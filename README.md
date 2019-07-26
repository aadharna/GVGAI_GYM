# GVGAI GYM

An [OpenAI Gym](gym.openai.com) environment for games written in the [Video Game Description Language](http://www.gvgai.net/vgdl.php), including the [Generic Video Game Competition](http://www.gvgai.net/) framework. 

The framework, along with some initial reinforcement learning results, is covered in the paper [Deep Reinforcement Learning for General Video Game AI](https://arxiv.org/abs/1806.02448). This paper should be cited if code from this project is used in any way:

```
@inproceedings{torrado2018deep,
  title={Deep Reinforcement Learning for General Video Game AI},
  author={Torrado, Ruben Rodriguez and Bontrager, Philip and Togelius, Julian and Liu, Jialin and Perez-Liebana, Diego},
  booktitle={Computational Intelligence and Games (CIG), 2018 IEEE Conference on},
  year={2018},
  organization={IEEE}
}
```

## Installation

- Clone this repository to your local machine.
- To install the package, run `pip install -e <package-location>`
  (This should install OpenAI Gym automatically, otherwise it can be installed [here](https://github.com/openai/gym)
- Install a Java compiler `javac` (e.g. `sudo apt install openjdk-11-jdk-headless`)

## Usage

Demo video on [YouTube](https://youtu.be/O84KgRt6AJI)

Once installed, it can be used like any OpenAI Gym environment.

Run the following line to get a list of all GVGAI environments.
```Python
[env.id for env in gym.envs.registry.all() if env.id.startswith('gvgai')]
```

### Custom levels

There are 5 levels pre-generated per environment, but you can also generate and use your own levels using the VGDL language.

To use a custom level for a particular game, you first have to use the `custom` level in your environment id. For example `gvgai-sokoban-custom-v0`.

You can then set the level data in the `gym.make` command using the `level_data` keyword argument.

```
gym.make('gvgai-sokoban-custom-v0', level_data=[your level data])
```

The `level_data` variable is a string containing the VGDL level definition.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/rubenrtorrado/GVGAI_GYM.

## License

This code is available as open source under the terms of the [Apache License 2.0](https://opensource.org/licenses/Apache-2.0).


