

def get_action_by_value(value, actions):
    for action in actions:
        if value == action.value:
            return action

    return None