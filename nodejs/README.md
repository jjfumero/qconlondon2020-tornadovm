Simple example of Graal Node calling into Java which uses tornado.

This example requires TornadoVM to be installed with GraalVM. See instructions [here](https://github.com/beehive-lab/TornadoVM/blob/master/assembly/src/docs/10_INSTALL_WITH_GRAALVM.md)

To reproduce this artefact (nodejs program with TornadoVM), run the following commands:

```bash
$ cd nodejs
$ export BASE=</path/to/tornadovm-graal>
$ export PATH=$BASE/bin/bin:$PATH
$ export TORNADO_SDK=$BASE/bin/sdk

## JDK 8 - last Graal
$ export JAVA_HOME=/path/to/oracleGraal/19.3.0/graalvm-ce-java8-19.3.0

## Install the NPM dependencies
$ $JAVA_HOME/bin/npm install express
$ $JAVA_HOME/bin/npm install jimp
$ $JAVA_HOME/bin/npm install fs
```

Compile the user-code using the alias for `javac` provided by TornadoVM:

```bash
$ javac.py Mandelbrot.java
```

##### Running with docker?

```bash
$ docker run \
 --runtime=nvidia \
 --rm -it -v \
 "$PWD":/data \
 beehivelab/tornado-gpu-graalvm-jdk11 \
 bash node.sh server.js

## Access
http://172.17.0.2:3000/ 
```

##### Running stand-alone?

```bash
$ bash node.sh server.js

## Access
http://127.0.0.1:3000/
```