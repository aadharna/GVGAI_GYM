import json
import logging
import sys
import os
import time
import numpy as np
import tempfile
import shutil

from CompetitionParameters import CompetitionParameters
from ElapsedCpuTimer import ElapsedCpuTimer
from IOSocket import IOSocket
from Types import LEARNING_SSO_TYPE, GAME_STATE


class ClientCommGYM:
    """
     * Client communication, set up the socket for a given agent
    """

    def __init__(self, game, version, lvl, pathStr):
        self.tempDir = tempfile.TemporaryDirectory()
        self.addLevel('')  # Level template to be loaded into java

        self.TOKEN_SEP = '#'
        self.io = IOSocket(self.tempDir.name)
        self.LOG = False
        self.player = None
        self.global_ect = None
        self.lastSsoType = LEARNING_SSO_TYPE.DATA
        self.terminal = False

        self.running = False

        baseDir = os.path.join(pathStr, 'gvgai')
        srcDir = os.path.join(baseDir, 'src')
        buildDir = os.path.join(baseDir, 'GVGAI_Build')
        gamesDir = os.path.join(pathStr, 'games', '{}_v{}'.format(game, version))
        cmd = ["java", "-Dsun.java2d.opengl=true", "-classpath", buildDir, "tracks.singleLearning.utils.JavaServer",
               "-game", game, "-gamesDir", gamesDir, "-imgDir", baseDir, "-portNum", str(self.io.port)]

        # Check build version
        # sys.path.append(baseDir)
        # import check_build
        #
        # if(not os.path.isdir(buildDir)):
        #     raise Exception("Couldn't find build directory. Please run build.py from the install directory or reinstall with pip.")
        # elif(not check_build.isCorrectBuild(srcDir, buildDir)):
        #     raise Exception("Your build is out of date. Please run build.py from the install directory or reinstall with pip.")
        # else:
        # try:
        #     self.java = subprocess.Popen(cmd, stdout=subprocess.DEVNULL, cwd=self.tempDir.name)
        # except subprocess.CalledProcessError as e:
        #     print('exit code: {}'.format(e.returncode))
        #     print('stderr: {}'.format(e.stderr.decode(sys.getfilesystemencoding())))

        self.startComm()
        self.reset(lvl)

    def startComm(self):
        self.io.initBuffers()
        # Reset currently sends initial commucations (which can't handle levels) and then resets
        # This should be split into two functions after the competition (July 18, 2018)
        self.reset(0)

    """
     * Method that perpetually listens for messages from the server.
     * With the use of additional helper methods, this function interprets
     * messages and represents the core response-generation methodology of the agent.
     * @throws IOException
    """

    def step(self, act):

        start = time.time()

        if not self.sso.Terminal:
            if (act == 0):
                self.act("")
            else:
                action = self.sso.availableActions[act - 1]
                self.act(action)
            self._game_phase, data = self.io.recv()
            self.process_data(self._game_phase, data)

            score = self.reward()
            self.lastScore = self.sso.gameScore
        else:
            score = 0

        if self.sso.isGameOver == True or self.sso.gameWinner == 'PLAYER_WINS' or self.sso.phase == "FINISH" or self.sso.phase == "ABORT" or self.sso.phase == "End":
            width = int(self.sso.worldDimension[1])
            height = int(self.sso.worldDimension[0])
            self.sso.image = np.zeros((width, height, 3))
            self.terminal = True
            # self.lastScore=0
            # Score = self.lastScore
            # self.lastScore=self.sso.gameScore
        else:
            self.terminal = False
            actions = self.actions()

        end = time.time()
        print('step: %d' % ((end - start) * 1000))

        if not hasattr(self.sso, 'image'):
            self.sso.image = np.zeros((100, 100, 3))

        info = {'winner': self.sso.gameWinner, 'actions': self.actions()}
        return self.sso.image, score, self.sso.Terminal, info

    def reset(self, lvl):
        self.lastScore = 0

        initialising = True

        # If we are already running then either we have overspent our time or the level has ended and we need to set a new level
        if self.running:
            if self.terminal:
                self.io.writeToServer(str(lvl).encode('utf-8'), self.lastSsoType, self.LOG)
            else:
                self.io.writeToServer("END_OVERSPENT".encode('utf-8'), log=self.LOG)

                self._game_phase, data = self.io.readFromServer()
                self.process_data(self._game_phase, data)

                self.io.writeToServer(str(lvl).encode('utf-8'), self.lastSsoType, self.LOG)

        # Keep reading from the server until we get the initial state
        while initialising:

            self._game_phase, data = self.io.readFromServer()
            game_state = self.process_data(self._game_phase, data)

            # Not really choosing a level, but using this as a start state
            if self._game_phase == GAME_STATE.CHOOSE_LEVEL:
                self.start()

            elif self._game_phase == GAME_STATE.INIT_STATE:
                self.init()
                initialising = False

        # Currently initial observation is not sent back on reset
        return np.zeros((100, 100, 3))

    def reward(self):
        scoreDelta = self.sso.gameScore - self.lastScore
        return scoreDelta

    def actions(self):
        nil = ["ACTION_NIL"]
        return nil + self.sso.availableActions

    def as_sso(self, d):
        self.sso.__dict__.update(d)
        return self.sso

    # def parse_json(self, input):
    #     parsed_input = json.loads(input)
    #     self.sso.__dict__.update(parsed_input)
    #     if parsed_input.get('observationGrid'):
    #         self.sso.observationGrid = [[[None for j in range(self.sso.observationGridMaxCol)]
    #                                      for i in range(self.sso.observationGridMaxRow)]
    #                                     for k in range(self.sso.observationGridNum)]
    #         for i in range(self.sso.observationGridNum):
    #             for j in range(len(parsed_input['observationGrid'][i])):
    #                 for k in range(len(parsed_input['observationGrid'][i][j])):
    #                     self.sso.observationGrid[i][j][k] = Observation(parsed_input['observationGrid'][i][j][k])
    #
    #     if parsed_input.get('NPCPositions'):
    #         self.sso.NPCPositions = [[None for j in
    #                                   range(self.sso.NPCPositionsMaxRow)] for i in
    #                                  range(self.sso.NPCPositionsNum)]
    #         for i in range(self.sso.NPCPositionsNum):
    #             for j in range(len(parsed_input['NPCPositions'][i])):
    #                 self.sso.NPCPositions[i][j] = Observation(parsed_input['NPCPositions'][i][j])
    #
    #     if parsed_input.get('immovablePositions'):
    #         self.sso.immovablePositions = [[None for j in
    #                                         range(self.sso.immovablePositionsMaxRow)] for i in
    #                                        range(self.sso.immovablePositionsNum)]
    #         for i in range(self.sso.immovablePositionsNum):
    #             for j in range(len(parsed_input['immovablePositions'][i])):
    #                 self.sso.immovablePositions[i][j] = Observation(parsed_input['immovablePositions'][i][j])
    #
    #     if parsed_input.get('movablePositions'):
    #         self.sso.movablePositions = [[None for j in
    #                                       range(self.sso.movablePositionsMaxRow)] for i in
    #                                      range(self.sso.movablePositionsNum)]
    #         for i in range(self.sso.movablePositionsNum):
    #             for j in range(len(parsed_input['movablePositions'][i])):
    #                 self.sso.movablePositions[i][j] = Observation(parsed_input['movablePositions'][i][j])
    #
    #     if parsed_input.get('resourcesPositions'):
    #         self.sso.resourcesPositions = [[None for j in
    #                                         range(self.sso.resourcesPositionsMaxRow)] for i in
    #                                        range(self.sso.resourcesPositionsNum)]
    #         for i in range(self.sso.resourcesPositionsNum):
    #             for j in range(len(parsed_input['resourcesPositions'][i])):
    #                 self.sso.resourcesPositions[i][j] = Observation(parsed_input['resourcesPositions'][i][j])
    #
    #     if parsed_input.get('portalsPositions'):
    #         self.sso.portalsPositions = [[None for j in
    #                                       range(self.sso.portalsPositionsMaxRow)] for i in
    #                                      range(self.sso.portalsPositionsNum)]
    #         for i in range(self.sso.portalsPositionsNum):
    #             for j in range(len(parsed_input['portalsPositions'][i])):
    #                 self.sso.portalsPositions[i][j] = Observation(parsed_input['portalsPositions'][i][j])
    #
    #     if parsed_input.get('fromAvatarSpritesPositions'):
    #         self.sso.fromAvatarSpritesPositions = [[None for j in
    #                                                 range(self.sso.fromAvatarSpritesPositionsMaxRow)] for i in
    #                                                range(self.sso.fromAvatarSpritesPositionsNum)]
    #         for i in range(self.sso.fromAvatarSpritesPositionsNum):
    #             for j in range(len(parsed_input['fromAvatarSpritesPositions'][i])):
    #                 self.sso.fromAvatarSpritesPositions[i][j] = Observation(parsed_input['fromAvatarSpritesPositions'][i][j])

    def process_data(self, game_state, data=None):

        try:

            print('GAME STATE: %s' % game_state)
            # assert game_state == GAME_STATE.ACT_STATE
            # if game_state == GAME_STATE.INIT_STATE or game_state:
            # elif game_state == "FINISH":
            #     self.sso.phase = Phase.FINISH
            # else:
            #     js.replace('"', '')
            #     self.parse_json(js)
            #     # self.sso = json.loads(js, object_hook=self.as_sso)
            # if self.sso.phase == "ACT":
            #     if(self.lastSsoType == LEARNING_SSO_TYPE.IMAGE or self.lastSsoType == "IMAGE" \
            #             or self.lastSsoType == LEARNING_SSO_TYPE.BOTH or self.lastSsoType == "BOTH"):
            #         if(self.sso.imageArray):
            #
            #             width = int(self.sso.worldDimension[1])
            #             height = int(self.sso.worldDimension[0])
            #             # self.sso.image = np.zeros((110,300,3))
            #             self.sso.image = np.reshape(np.array(self.sso.imageArray, dtype=np.uint8), (width,height,3))
            # self.sso.convertBytesToPng(self.sso.imageArray, self.tempDir.name)
            # self.sso.image = misc.imread(os.path.join(self.tempDir.name, 'gameStateByBytes.png'))

        except Exception as e:
            logging.exception(e)
            print("Line processing [FAILED]")
            # traceback.print_exc()
            sys.exit()

    """
     * Manages the start of the communication. It starts the whole process, and sets up the timer for the whole run.
    """

    def start(self):
        self.global_ect = ElapsedCpuTimer()
        self.global_ect.setMaxTimeMillis(CompetitionParameters.TOTAL_LEARNING_TIME)
        ect = ElapsedCpuTimer()
        ect.setMaxTimeMillis(CompetitionParameters.START_TIME)
        # self.startAgent()

        if ect.exceededMaxTime():
            self.io.writeToServer("START_FAILED".encode('utf-8'), self.LOG)
        else:
            self.io.writeToServer("START_DONE".encode('utf-8'), self.lastSsoType, self.LOG)

    def init(self):
        ect = ElapsedCpuTimer()
        ect.setMaxTimeMillis(CompetitionParameters.INITIALIZATION_TIME)
        self.lastSsoType = LEARNING_SSO_TYPE.IMAGE

        if ect.exceededMaxTime():
            self.io.writeToServer("INIT_FAILED".encode('utf-8'), self.LOG)
        else:
            self.io.writeToServer("INIT_DONE".encode('utf-8'),  self.lastSsoType, self.LOG)

    """
     * Manages the action request for an agent. The agent is requested for an action,
     * which is sent back to the server
    """

    def act(self, action):
        start = time.time()
        ect = ElapsedCpuTimer()
        ect.setMaxTimeMillis(CompetitionParameters.ACTION_TIME)
        # action = str(self.player.act(self.sso, ect.copy()))
        if (not action) or (action == ""):
            action = "ACTION_NIL"
        # self.lastSsoType = self.player.lastSsoType
        self.lastSsoType = LEARNING_SSO_TYPE.IMAGE

        if ect.exceededMaxTime():
            if ect.elapsedNanos() > CompetitionParameters.ACTION_TIME_DISQ * 1000000:
                self.io.writeToServer("END_OVERSPENT", self.LOG)
            else:
                self.io.writeToServer("ACTION_NIL" + "#" + self.lastSsoType, self.LOG)
        else:
            self.io.writeToServer(action + "#" + self.lastSsoType, self.LOG)

        end = time.time()
        print('act: %d' % ((end - start) * 1000))

    def addLevel(self, path):
        lvlName = os.path.join(self.tempDir.name, 'game_lvl5.txt')
        if (path is ''):
            open(lvlName, 'w+').close()
        else:
            shutil.copyfile(path, lvlName)

    def __del__(self):
        try:
            self.java.kill()
        except:
            pass
