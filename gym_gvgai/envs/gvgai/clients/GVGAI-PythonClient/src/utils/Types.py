
class ACTIONS:
    """
     * All action types, corresponding to the server Java code ontology.Types
    """

    def __init__(self):
        pass

    ACTION_NIL = bytes([0])
    ACTION_UP = bytes([1])
    ACTION_LEFT = bytes([2])
    ACTION_DOWN = bytes([3])
    ACTION_RIGHT = bytes([4])
    ACTION_USE = bytes([5])
    ACTION_ESCAPE = bytes([6])


class WINNER:
    """
     * Winner/Loser types, corresponding to the server Java code ontology.Types
    """

    def __init__(self):
        pass

    PLAYER_DISQ = -100
    NO_WINNER = -1
    PLAYER_LOSES = 0
    PLAYER_WINS = 1


class LEARNING_SSO_TYPE():
    def __init__(self):
        pass

    IMAGE = bytes([0])
    DATA = bytes([1])
    BOTH = bytes([2])

class GAME_STATE:

    def __init__(self):
        pass

    INIT_STATE = bytes([0])
    ACT_STATE = bytes([1])
    END_STATE = bytes([2])
    ABORT_STATE = bytes([3])
    CHOOSE_LEVEL = bytes([4])