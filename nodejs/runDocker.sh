#!/bin/bash


## With a local image:
#docker run --runtime=nvidia --rm -it -v "$PWD":/data graalvm bash node.sh server.js

## Official Docker-image for TornadoVM 0.6 and GraalVM JDK 11
echo "docker run --runtime=nvidia --rm -it -v "$PWD":/data beehivelab/tornado-gpu-graalvm-jdk11 bash node.sh server.js"
docker run --runtime=nvidia --rm -it -v "$PWD":/data beehivelab/tornado-gpu-graalvm-jdk11 bash node.sh server.js
