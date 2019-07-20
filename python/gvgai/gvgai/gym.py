#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Simulate VGDL Games
"""
import gym
import numpy as np
from gym import spaces

from gvgai import GVGAIClient


class GVGAI_Env(gym.Env):
    """
    Define a VGDL environment.
    The environment defines which actions can be taken at which point and
    when the agent receives which reward.
    """

    # Static client that we re-use if the level changes
    gvgai_client = None

    @staticmethod
    def get_client(client_only=False):
        if GVGAI_Env.gvgai_client is None:
            GVGAI_Env.gvgai_client = GVGAIClient(client_only=client_only)
        return GVGAI_Env.gvgai_client

    @staticmethod
    def stop_client():
        if GVGAI_Env.gvgai_client is not None:
            GVGAI_Env.gvgai_client.stop()

    def __init__(self, environment_id=None, client_only=False):
        self.__version__ = "0.0.2"
        metadata = {'render.modes': ['human', 'rgb_array']}

        # Get or create the client and set the environment
        self.GVGAI = GVGAI_Env.get_client(client_only)
        self.GVGAI.reset(environment_id)

        self.environment_id = environment_id
        self.actions = self.GVGAI.actions
        self.world_dimensions = self.GVGAI.world_dimensions
        self.viewer = None

        # Only allow gridphysics games for now
        # Get number of moves for a selected game
        self.action_space = spaces.Discrete(len(self.actions))

        height, width = self.world_dimensions

        observation_space = np.array([width, height, 3])

        # Observation is the remaining time
        self.observation_space = spaces.Box(low=0, high=255, shape=observation_space, dtype=np.int32)

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

    def reset(self, environment_id=None):
        """
        Reset the state of the environment and returns an initial observation.
        Returns
        -------
        observation (object): the initial observation of the space.
        """

        if environment_id is not None:
            self._set_environment_id(environment_id)

        self.img = self.GVGAI.reset(self.environment_id)
        return self.img

    def render(self, mode='human'):
        if self.img is None:
            return

        if mode == 'rgb_array':
            return self.img
        elif mode == 'human':
            if not self.viewer:
                self.viewer = SimpleImageViewer(maxwidth=500)
            self.viewer.imshow(self.img)
            return self.viewer.isopen

    def close(self):
        if self.viewer is not None:
            self.viewer.close()
            self.viewer = None

    # Expects path string or int value
    def _set_environment_id(self, environment_id):
        self.close()
        self.environment_id = environment_id

    def get_action_meanings(self):
        return self.actions

    def __del__(self):
        self.close()

class SimpleImageViewer(object):


    def __init__(self, display=None, maxwidth=500):
        self.window = None
        self.isopen = False
        self.display = display
        self.maxwidth = maxwidth

        self._pyglet = __import__('pyglet')
        self._gl = self._pyglet.gl

    def imshow(self, arr):
        if self.window is None:
            height, width, _channels = arr.shape
            # if width > self.maxwidth:
            scale = self.maxwidth / width
            width = int(scale * width)
            height = int(scale * height)
            self.window = self._pyglet.window.Window(width=width, height=height,
                                               display=self.display, vsync=False, resizable=True)
            self.width = width
            self.height = height
            self.isopen = True

            @self.window.event
            def on_resize(width, height):
                self.width = width
                self.height = height

            @self.window.event
            def on_close():
                self.isopen = False

        assert len(arr.shape) == 3, "You passed in an image with the wrong number shape"
        image = self._pyglet.image.ImageData(arr.shape[1], arr.shape[0],
                                       'RGB', arr.tobytes(), pitch=arr.shape[1] * -3)
        self._gl.glTexParameteri(self._gl.GL_TEXTURE_2D,
                           self._gl.GL_TEXTURE_MAG_FILTER, self._gl.GL_NEAREST)
        texture = image.get_texture()
        texture.width = self.width
        texture.height = self.height
        self.window.clear()
        self.window.switch_to()
        self.window.dispatch_events()
        texture.blit(0, 0)  # draw
        self.window.flip()

    def close(self):
        self.window.close()
        self.isopen = False

    def __del__(self):
        self.close()
