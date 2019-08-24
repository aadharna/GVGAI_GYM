from enum import Enum


class Action(Enum):
    ACTION_NIL = 0
    ACTION_UP = 1
    ACTION_LEFT = 2
    ACTION_DOWN = 3
    ACTION_RIGHT = 4
    ACTION_USE = 5

class Winner(Enum):
    PLAYER_DISQ = -100
    NO_WINNER = -1
    PLAYER_LOSES = 0
    PLAYER_WINS = 1

class GamePhase(Enum):
    START_STATE = 0
    CHOOSE_LEVEL = 1
    INIT_STATE = 2
    ACT_STATE = 3
    OBSERVE_STATE = 4
    END_STATE = 5
    ABORT_STATE = 6

class AgentPhase(Enum):
    START_STATE = 0
    CHOOSE_LEVEL_STATE = 1
    INIT_STATE = 2
    ACT_STATE = 3
    OBSERVE_STATE = 4
    END_STATE = 5
    ABORT_STATE = 6
