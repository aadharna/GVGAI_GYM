import logging
import time
import numpy as np

from gvgai.gym import GVGAI_Env
from gvgai.utils.level_data_generator import SokobanGenerator

"""
A test client that connects directly to the server environment and does not start one itself
This can be used for debugging
"""

if __name__ == '__main__':

    # Turn debug logging on
    logging.basicConfig(level=logging.INFO)

    logger = logging.getLogger('Test Agent')

    level_generator = SokobanGenerator()

    environments = [
        'sokoban-custom'
    ]

    config = {
        'prob_hole': 0.05,
        'prob_box': 0.05,
        'prob_wall': 0.3,
        'width': np.random.randint(4, 5),
        'height': np.random.randint(4, 5)
    }

    for i in range(100):
        for environment_id in environments:

            level_data = level_generator.generate(1, config).__next__()

            # This should reuse the underlying client
            env = GVGAI_Env(environment_id, level_data=level_data, tile_observations=True, client_only=True)

            actions = env.unwrapped.get_action_meanings()

            start = time.time()
            frames = 0

            for t in range(10000):
                # choose action based on trained policy
                # do action and get new state and its reward
                action_id = np.random.randint(5)
                stateObs, diffScore, done, debug = env.step(action_id)

                env.render()

                #time.sleep(1)

                frames += 1

                if t % 1000 == 0:
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
    logger.debug(f'frames per second: {fps}')

GVGAI_Env.stop_client()