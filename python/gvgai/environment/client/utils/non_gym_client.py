import logging
import time
import numpy as np

from gvgai.environment.gvgai_gym import GVGAI_Env

"""
A test client that connects directly to the server environment and does not start one itself
"""

if __name__ == '__main__':


    # Turn debug logging on
    logging.basicConfig(level=logging.DEBUG)

    logger = logging.getLogger('Test Agent')

    env = GVGAI_Env('sokoban-lvl0', client_only=True)

    actions = env.unwrapped.get_action_meanings()
    start = time.time()
    frames = 0
    for t in range(20000):
        # choose action based on trained policy
        # do action and get new state and its reward
        action_id = np.random.randint(5)
        stateObs, diffScore, done, debug = env.step(action_id)
        env.render()

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