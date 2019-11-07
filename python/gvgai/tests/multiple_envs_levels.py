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


    def generate_level():
        config = {
            'prob_hole': 0.05,
            'prob_box': 0.05,
            'prob_wall': 0.3,
            'width': np.random.randint(4, 10),
            'height': np.random.randint(4, 10)
        }

        return level_generator.generate(1, config).__next__()


    start = time.time()
    frames = 0

    num_envs = 3

    envs = [
        GVGAI_Env('sokoban-custom', tile_observations=True, level_data=generate_level())
        for e in range(num_envs)
    ]

    for i in range(100):

        for env in envs:
            env.reset(level_data=generate_level())

        for t in range(100):
            # choose action based on trained policy
            # do action and get new state and its reward
            action_id = np.random.randint(5)

            steps = [env.step(action_id) for env in envs]

            for env in envs:
                env.render()

            frames += 1

            if t % 10 == 0:
                end = time.time()
                total_time = end - start
                fps = (frames / total_time)
                logger.info(f'frames per second: {fps * num_envs}')

                start = time.time()
                frames = 0

            for step, env in zip(steps, envs):
                if step[2]:
                    env.reset()
