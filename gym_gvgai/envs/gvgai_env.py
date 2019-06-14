#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Simulate VGDL Games
"""
import sys
from os import path
import numpy as np

dir = path.dirname(__file__)
gvgai_path = path.join(dir, "gvgai", "clients", "GVGAI-PythonClient", "src", "utils")
sys.path.append(gvgai_path)

import gym
from gym import error, spaces, utils
import ClientCommGYM as gvgai

class GVGAI_Env(gym.Env):
    """
    Define a VGDL environment.
    The environment defines which actions can be taken at which point and
    when the agent receives which reward.
    """

    def __init__(self, game, level, version):
        self.__version__ = "0.0.2"
        metadata = {'render.modes': ['human', 'rgb_array']}

        #Send the level to play
        self.GVGAI = gvgai.ClientCommGYM(game, version, level, dir)
        self.game = game
        self.level = level
        self.version = version
        self.actions = self.GVGAI.actions
        self.world_dimensions = self.GVGAI.world_dimensions
        self.viewer = None
       
        #Only allow gridphysics games for now
        #Get number of moves for a selected game
        self.action_space = spaces.Discrete(len(self.actions))

        # Observation is the remaining time
        self.observation_space = spaces.Box(low=0, high=255, shape=self.world_dimensions, dtype=np.int32)
        
    def step(self, action):
        """
        The agent takes a step in the environment.
        Parameters
        ----------
        action : int
        Returns
        -------
        ob, reward, episode_over, info : tuple
            state (image) :
                An image of the current frame of the game
            reward (float) :
                Total reward (Philip: Should it be incremental reward? Check Atari)
            isOver (bool) :
                whether it's time to reset the environment again.
            info (dict):
                info that can be added for debugging
                info["winner"] == PLAYER_LOSES, PLAYER_WINS, NO_WINNER
        """
        state, reward, isOver, info = self.GVGAI.step(action)
        
        self.img = state
        return state, reward, isOver, info

    def reset(self):
        """
        Reset the state of the environment and returns an initial observation.
        Returns
        -------
        observation (object): the initial observation of the space.
        """
        self.img = self.GVGAI.reset(self.level)
        return self.img

    def render(self, mode='human'):
        if self.img is None:
            return

        if mode == 'rgb_array':
            return self.img
        elif mode == 'human':
            from gym.envs.classic_control import rendering
            if self.viewer is None:
                self.viewer = rendering.SimpleImageViewer()
            self.viewer.imshow(self.img)
            return self.viewer.isopen

    def close(self):
        if self.viewer is not None:
            self.viewer.close()
            self.viewer = None

    #Expects path string or int value
    def _setLevel(self, level):
        if(type(level) == int):
            if(level < 5):
                self.level = level
            else:
                print("Level doesn't exist, playing level 0")
                self.level = 0
        else:
            newLvl = path.realpath(level)
            ogLvls = [path.realpath(path.join(dir, 'games', '{}_v{}'.format(self.game, self.version), '{}_lvl{}.txt'.format(self.game, i))) for i in range(5)]
            if(newLvl in ogLvls):
                lvl = ogLvls.index(newLvl)
                self.level = lvl
            elif(path.exists(newLvl)):
                self.GVGAI.addLevel(newLvl)
                self.level = 5
            else:
                print("Level doesn't exist, playing level 0")
                self.level = 0

    def get_action_meanings(self):
        return self.actions
