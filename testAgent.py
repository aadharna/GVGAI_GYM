#!/usr/bin/env python
import gym
import gym_gvgai
import time
import Agent as Agent

# Predefined names referring to framework
games = ['gvgai-testgame1', 'gvgai-testgame2', 'gvgai-testgame3']
trainingLevels = ['lvl0-v0', 'lvl1-v0']
testLevels = ['lvl2-v0', 'lvl3-v0', 'lvl4-v0']

for game in games:
    for level in trainingLevels: #testLevels:
        env = gym_gvgai.make(game + '-' + level)
        agent = Agent.Agent()
        print('Starting ' + env.env.game + " with Level " + str(env.env.level))
        # reset environment
        stateObs = env.reset()
        actions = env.unwrapped.get_action_meanings()
        start = time.time()
        frames = 0
        for t in range(2000):
            # choose action based on trained policy
            action_id = agent.act(stateObs, actions)
            # do action and get new state and its reward
            stateObs, diffScore, done, debug = env.step(action_id)
            env.render()
            # print("Action " + str(action_id) + " tick " + str(t+1) + " reward " + str(diffScore) + " win " + str(debug["winner"]))

            frames+=1
            # break loop when terminal state is reached
            if done:
                env.reset()
        end = time.time()

        total_time = end-start

        fps = (frames / total_time)

        print("frames per second: %d" % int(fps))
