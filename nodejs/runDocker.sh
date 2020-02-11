#!/bin/bash

## Note: this script is taken a local image (graalvm) with TornadoVM and Graal compilers in it

docker run --runtime=nvidia --rm -it -v "$PWD":/data graalvm bash node.sh server.js
