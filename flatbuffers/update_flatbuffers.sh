#!/bin/bash

# Generate Java Code
flatc -j -o java/core/game/serialization serializableFlatBuffer.fbs

# Generate Python code
flatc -p -o python/ serializableFlatBuffer.fbs