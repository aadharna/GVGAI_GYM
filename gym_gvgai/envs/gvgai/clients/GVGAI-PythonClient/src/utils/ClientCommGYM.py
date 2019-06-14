import logging
import sys
import os
import numpy as np
import tempfile
import subprocess
import shutil
from struct import pack_into

from IOSocket import IOSocket
from Types import GamePhase, Action, AgentPhase
from serialization import State


class ClientCommGYM:
    """
     * Client communication, set up the socket for a given agent
    """


    def _get_libs(self, path):
        libs = []
        for root, _, files in os.walk(path):
            for f in files:
                if(f.endswith('.jar')):
                    libs.append(os.path.join(root, f))
        return libs

    def __init__(self, game, version, level, pathStr, request_image=True):
        self.tempDir = tempfile.TemporaryDirectory()

        self.TOKEN_SEP = '#'
        self.io = IOSocket(self.tempDir.name)
        self.LOG = False
        self.player = None
        self.global_ect = None
        self.terminal = False
        self._running = False
        self._request_image = request_image
        self.actions = []
        self.level = level



        baseDir = os.path.join(pathStr, 'gvgai')
        srcDir = os.path.join(baseDir, 'src')
        jarDir = self._get_libs(os.path.join(baseDir, 'lib'))
        buildDir = os.path.join(baseDir, 'GVGAI_Build')
        gamesDir = os.path.join(pathStr, 'games', '{}_v{}'.format(game, version))

        fullClasspath = ':'.join(jarDir + [buildDir])

        cmd = ["java", "-Dsun.java2d.opengl=true", "-classpath", fullClasspath, "tracks.singleLearning.utils.JavaServer",
               "-game", game, "-gamesDir", gamesDir, "-imgDir", baseDir, "-portNum", str(self.io.port)]

        #Check build version
        sys.path.append(baseDir)
        import check_build

        if(not os.path.isdir(buildDir)):
            raise Exception("Couldn't find build directory. Please run build.py from the install directory or reinstall with pip.")
        elif(not check_build.isCorrectBuild(srcDir, buildDir)):
            raise Exception("Your build is out of date. Please run build.py from the install directory or reinstall with pip.")
        else:
            try:
                self.java = subprocess.Popen(cmd, stdout=subprocess.DEVNULL, cwd=self.tempDir.name)
            except subprocess.CalledProcessError as e:
                print('exit code: {}'.format(e.returncode))
                print('stderr: {}'.format(e.stderr.decode(sys.getfilesystemencoding())))

        self.io.initBuffers()

        # Firstly we should receive a choose-level state
        game_phase, state, image = self._read_and_process_server_response()
        assert game_phase == GamePhase.START_STATE, "Expecting START_STATE from GVGAI, but received %s" % GamePhase(
            game_phase)
        self._start()

        self.reset(level)

    """
     * Method that perpetually listens for messages from the server.
     * With the use of additional helper methods, this function interprets
     * messages and represents the core response-generation methodology of the agent.
     * @throws IOException
    """

    def step(self, act):

        if self.terminal:
            return

        game_phase, state, image = self._read_and_process_server_response()
        self.io.writeToServer(AgentPhase.ACT_STATE, act.to_bytes(4, byteorder='big'), self.LOG)

        current_score, reward = self._get_reward(state)
        self._previous_score = current_score

        actions = self._get_actions(state)

        if state.IsGameOver() == True or state.GameWinner() == 'PLAYER_WINS' or game_phase == GamePhase.END_STATE or game_phase == GamePhase.END_STATE:
            self.terminal = True
        else:
            self.terminal = False

        info = {'winner': state.GameWinner(), 'actions': actions}
        return image, reward, self.terminal, info

    def reset(self, level):
        self._previous_score = 0
        self.image = None

        # If this is already running then we abort the game
        if self._running:
            game_phase, state, image = self._read_and_process_server_response()
            assert game_phase == GamePhase.ACT_STATE, "Expecting ACT_STATE from GVGAI, but received %s" % GamePhase(game_phase)
            self._abort_game()

        # Firstly we should receive a choose-level state
        game_phase, state, image = self._read_and_process_server_response()
        assert game_phase == GamePhase.CHOOSE_LEVEL, "Expecting CHOOSE_LEVEL from GVGAI, but received %s" % GamePhase(game_phase)
        self._choose_level(level)

        # Secondly we should receive an init state
        game_phase, state, image = self._read_and_process_server_response()
        assert game_phase == GamePhase.INIT_STATE, "Expecting INIT_STATE from GVGAI, but received %s" % GamePhase(game_phase)
        self._init(state)
        self._running = True

        # Currently initial observation is not sent back on reset
        if image is None:
            print('No image sent in initial state')
            width, height = self._get_dimensions(state)
            image = np.zeros((height, width, 3))

        return image

    def _get_dimensions(self, state):
        dims = state.WorldDimensionAsNumpy().astype(np.int32)
        return dims[0], dims[1]

    def _read_and_process_server_response(self):
        game_phase_bytes, data_bytes = self.io.readFromServer()
        game_phase = GamePhase(int.from_bytes(game_phase_bytes, 'big'))
        state, image = self._process_data(data_bytes)
        return game_phase, state, image

    def _get_reward(self, state):
        current_score = state.GameScore()
        reward = current_score - self._previous_score
        return current_score, reward

    def _convert_to_actions(self, actions_numpy):
        return [Action(action) for action in actions_numpy]

    def _get_actions(self, state):
        availableActions = self._convert_to_actions(state.AvailableActionsAsNumpy())
        return availableActions + [Action.ACTION_NIL]

    def _process_data(self, data=None):

        try:
            if data is not None:

                state = State.GetRootAsState(data, 0)

                image = None
                if state.ImageArrayLength() != 0:
                    width, height = self._get_dimensions(state)
                    image = np.reshape(state.ImageArrayAsNumpy(), (height, width, 3))

                return state, image

            return None, None

        except Exception as e:
            logging.exception(e)
            print("Line processing [FAILED]")
            # traceback.print_exc()
            sys.exit()

    def _start(self):
        self.io.writeToServer(AgentPhase.START_STATE, log=self.LOG)

    def _abort_game(self):
        self.io.writeToServer(AgentPhase.ABORT_STATE, log=self.LOG)
        self._end_game()

    def _end_game(self):
        game_phase, state, image = self._read_and_process_server_response()
        assert game_phase == GamePhase.END_STATE, "Expecting END_STATE from GVGAI, but received %s" % GamePhase(game_phase)
        self.io.writeToServer(AgentPhase.END_STATE, log=self.LOG)

    def _choose_level(self, level):

        requires_image_byte = bytes([1]) if self._request_image else bytes([0])

        choose_level_data = bytearray(5)
        pack_into('>i', choose_level_data, 0, level)
        pack_into('c', choose_level_data, 4, requires_image_byte)

        self.io.writeToServer(AgentPhase.CHOOSE_LEVEL_STATE, choose_level_data, self.LOG)

    def _init(self, state):
        self.actions = self._get_actions(state)
        self.world_dimensions = state.WorldDimensionAsNumpy().astype(np.int32)
        self.io.writeToServer(AgentPhase.INIT_STATE, log=self.LOG)

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
