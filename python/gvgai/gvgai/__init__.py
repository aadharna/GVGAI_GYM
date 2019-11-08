import re
import os
from gym.envs.registration import register

from gvgai.client.gvgai_client import GVGAIClient

dir = os.path.dirname(__file__)
gamesPath = os.path.normpath(os.path.join(dir, '../../../games'))
games = os.listdir(gamesPath)

for game in games:
    gamePath = os.path.join(gamesPath, game)
    if (os.path.isdir(gamePath)):
        game_dir_regex = '(?P<name>.+)_v(?P<version>\d+)'
        game_parts = re.search(game_dir_regex, game)
        name = game_parts.group('name')
        version = game_parts.group('version')

        # Register all the levels which are in the directory
        level_filenames = [lvl for lvl in os.listdir(gamePath) if 'lvl' in lvl]
        for level_filename in level_filenames:
            level = level_filename.split('.')[0]

            level = level.replace('_', '-')
            register(
                id=f'gvgai-{level}-v{version}',
                entry_point='gvgai.gym:GVGAI_Env',
                kwargs={'environment_id': level},
                max_episode_steps=2000
            )

        level = f'{name}-custom'
        # Register the custom environment so any levels can be passed in
        register(
            id=f'gvgai-{level}-v{version}',
            entry_point='gvgai.gym:GVGAI_Env',
            kwargs={'environment_id': level},
            max_episode_steps=2000
        )
