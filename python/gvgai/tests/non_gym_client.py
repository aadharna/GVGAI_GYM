import logging
import time
import numpy as np

from gvgai.gym import GVGAI_Env
from gvgai.utils.level_data_generator import SokobanGenerator

if __name__ == '__main__':

    # Turn debug logging on
    logging.basicConfig(level=logging.INFO)

    logger = logging.getLogger('Test Agent')

    level_generator = SokobanGenerator()

    env = GVGAI_Env('sokoban-lvl0', max_steps=10, tile_observations=False, include_semantic_data=True, client_only=True)

    initial_frame = env.reset()

    actions = env.unwrapped.get_action_meanings()

    start = time.time()
    frames = 0

    for t in range(1000):
        # choose action based on trained policy
        # do action and get new state and its reward
        action_id = np.random.randint(5)
        stateObs, diffScore, done, debug = env.step(action_id)

        env.render()

        #time.sleep(1)

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