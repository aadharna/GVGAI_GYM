import logging
import random


class LevelGenerator():
    """
    Generates random level data
    """

    def __init__(self, name):
        self._name = name
        self._logger = logging.getLogger(name)

    def generate(self, num_levels, config):
        raise NotImplemented


class SokobanGenerator(LevelGenerator):
    """
    wwwwwwwwwwwww
    w........w..w
    w...1.......w
    w...A.1.w.0ww
    www.w1..wwwww
    w.......w.0.w
    w.1........ww
    w..........ww
    wwwwwwwwwwwww
    """

    def __init__(self):
        super().__init__("Sokoban")

    def _get_sprite(self, prob_wall, prob_box, prob_hole):

        sprite_select = random.uniform(0, 1)

        # Wall
        if sprite_select < prob_wall:
            return 'w'

        # Box
        if sprite_select < prob_box + prob_wall:
            return '1'

        # Hole
        if sprite_select < prob_box + prob_wall + prob_hole:
            return '0'

        # Empty space
        return '.'

    def _generate_single(self, config):

        prob_wall = config['prob_wall']
        prob_box = config['prob_box']
        prob_hole = config['prob_hole']

        assert prob_wall + prob_box + prob_hole < 1.0, 'Probabilities must not sum larger than 1'

        width = config['width']
        height = config['height']

        level_string_array = []

        # Randomly place walls
        for h in range(height):
            row_string_array = []
            for w in range(width):
                if w == 0 or h == 0 or h == height-1 or w == width-1:
                    row_string_array.append('w')
                else:
                    row_string_array.append(self._get_sprite(prob_wall, prob_box, prob_hole))
            level_string_array.append(row_string_array)

        # Add the agent within the outer walls
        x,y = random.randint(1, width-2), random.randint(1, height-2)
        level_string_array[y][x] = 'A'

        level_string_array = [''.join(r) for r in level_string_array]

        return '\n'.join(level_string_array)

    def generate(self, num_levels, config):

        for level in range(num_levels):
            yield self._generate_single(config)
