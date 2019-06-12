import json
import logging
import sys
import os
import time
import numpy as np
import tempfile
import shutil

from IOSocket import IOSocket
from Types import LEARNING_SSO_TYPE, GAME_STATE
from serialization import State


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

        self.io.initBuffers()
        self.reset()

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

    def reset(self):
        self.lastScore = 0
        self.image = None
        initialising = True

        # Keep reading from the server until we get the initial state
        while initialising:

            self._game_phase, data = self.io.readFromServer()
            self.process_data(self._game_phase, data)

            if self._game_phase == GAME_STATE.CHOOSE_LEVEL:
                self.start()

            elif self._game_phase == GAME_STATE.INIT_STATE:
                self.init()
                initialising = False

        if self.image is None:
            print('No image sent in initial state')

        # Currently initial observation is not sent back on reset
        return np.zeros((100, 100, 3))

    def reward(self):
        scoreDelta = self.sso.gameScore - self.lastScore
        return scoreDelta

    def actions(self):
        nil = ["ACTION_NIL"]
        return nil + self._state.AvailableActionsAsNumpy()

    def as_sso(self, d):
        self.sso.__dict__.update(d)
        return self.sso

    def process_data(self, game_state, data=None):

        try:

            print('GAME STATE: %s' % GAME_STATE.get_game_state_string(game_state))

            if data is not None:

                state = State.GetRootAsState(data, 0)

                image = None
                if game_state == GAME_STATE.ACT_STATE:
                    if (self.lastSsoType == LEARNING_SSO_TYPE.IMAGE or self.lastSsoType == LEARNING_SSO_TYPE.BOTH):
                        if state.ImageArrayLength() != 0:
                            width = int(state.worldDimension[1])
                            height = int(state.worldDimension[0])
                            # self.sso.image = np.zeros((110,300,3))
                            image = np.reshape(state.ImageArrayAsNumpy(), (width, height, 3))

                self._state = state
                self._image = image

        except Exception as e:
            logging.exception(e)
            print("Line processing [FAILED]")
            # traceback.print_exc()
            sys.exit()




    """
     * Manages the start of the communication. It starts the whole process, and sets up the timer for the whole run.
    """

    def start(self):
        self.io.writeToServer("START_DONE".encode('utf-8'), self.lastSsoType, self.LOG)

    def init(self):
        self.io.writeToServer("INIT_DONE".encode('utf-8'), self.lastSsoType, self.LOG)

    """
     * Manages the action request for an agent. The agent is requested for an action,
     * which is sent back to the server
    """

    def act(self, action):
        if (not action) or (action == ""):
            action = "ACTION_NIL"

        self.io.writeToServer(action.encode('utf-8'), self.lastSsoType, self.LOG)


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
