import logging
import gym
import gvgai
import time
import numpy as np

from gvgai.client.utils.agent import get_action_by_value
from gvgai.gym import GVGAI_Env

games = ['cookmepasta']
levels = ['lvl0-v0', 'lvl1-v0', 'lvl2-v0', 'lvl3-v0', 'lvl4-v0']

# Turn debug logging on
logging.basicConfig(level=logging.INFO)

logger = logging.getLogger("Test Agent")

for game in games:
    for level in levels:
        env = gym.make(f'gvgai-{game}-{level}', max_steps=300, pixel_observations=True, include_semantic_data=True)
        logger.info(f'Starting {env.spec.id}')
        # reset environment
        stateObs = env.reset()
        actions = env.unwrapped.get_action_meanings()
        start = time.time()
        frames = 0
        env.render()

        for t in range(100):
            # choose action based on trained policy
            # do action and get new state and its reward
            action_id = np.random.randint(5)

            action_description = get_action_by_value(action_id, actions)

            stateObs, diffScore, done, debug = env.step(action_id)
            env.render()

            frames += 1

            if t % 100 == 0:
                end = time.time()
                total_time = end - start
                fps = (frames / total_time)
                logger.info(f'frames per second: {fps}')

            # break loop when terminal state is reached
            if done:
                env.reset()

        end = time.time()
        total_time = end - start
        fps = (frames / total_time)
        logger.info(f'frames per second: {fps}')
