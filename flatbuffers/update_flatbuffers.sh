#!/bin/bash

# Generate Java Code
flatc -j -o core/game/serialization serializableFlatBuffer.fbs

# Generate Python code
flatc -p serializableFlatBuffer.fbs