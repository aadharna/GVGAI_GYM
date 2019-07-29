import logging
import time
import numpy as np

from gvgai.gym import GVGAI_Env
from gvgai.utils.level_data_generator import SokobanGenerator


"""
A test client that connects directly to the server environment and does not start one itself

This client tests that we can create custom levels through this API
"""

if __name__ == '__main__':

    # Turn debug logging on
    logging.basicConfig(level=logging.INFO)

    logger = logging.getLogger('Test Agent')

    level_generator = SokobanGenerator()

    start = time.time()
    frames = 0

    for i in range(10000):

        config = {
            'prob_hole': 0.05,
            'prob_box': 0.05,
            'prob_wall': 0.3,
            'width': np.random.randint(8, 20),
            'height': np.random.randint(8, 20)
        }
        for level_data in level_generator.generate(10, config):

            env = GVGAI_Env('sokoban-custom', level_data=level_data, client_only=True)

            actions = env.unwrapped.get_action_meanings()

            for t in range(10):
                # choose action based on trained policy
                # do action and get new state and its reward
                action_id = np.random.randint(5)
                stateObs, diffScore, done, debug = env.step(action_id)

                # env.render()

                frames += 1

                if t % 1000 == 0:
                    end = time.time()
                    total_time = end - start
                    fps = (frames / total_time)
                    logger.debug(f'frames per second: {fps}')

                # break loop when terminal state is reached
                if done:
                    env.reset()

    end = time.time()
    total_time = end - start
    fps = (frames / total_time)
    logger.debug(f'frames per second: {fps}')

GVGAI_Env.stop_client()
