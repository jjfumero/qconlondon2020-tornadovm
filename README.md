# TornadoVM QCon-London 2020 - Demos

Artefact & demos used for QCon-London presentation 2020: [TornadoVM @ QCon-London](https://qconlondon.com/london2020/presentation/tornadovm-breaking-programmability-and-usability-gap-between-java)

Slides available [here](https://github.com/jjfumero/jjfumero.github.io/blob/master/files/QCON2020-TornadoVM.pdf)

Video will be available soon by [InfoQ](https://www.infoq.com/)


###### Pre-requisites 

* Install TornadoVM. Full guideline [here](https://github.com/beehive-lab/TornadoVM/blob/master/INSTALL.md)
* OpenCL device with OpenCL >= 1.2 installed


## 1) Live Task Migration

Demo using the Client-Server application to change the devices through the client:

```bash
$ export JAVA_HOME=/PATH/TO/graal-jvmci-8/jdk1.8.0/product
$ export TORNADO_ROOT=/PATH/TO/TORNADOVM/ROOT
$ export PATH="${PATH}:${TORNADO_ROOT}/bin/bin/"
$ export TORNADO_SDK=${TORNADO_ROOT}/bin/sdk

## Compile the application
$ mvn clean package
```

How to reproduce?:

```bash
## Run Server in one terminal
./runServer.sh

## Client in another terminal
./runClient.sh
```

## 2) Matrix Multiplication


```bash
$ export CLASSPATH=target/tornado-1.0-SNAPSHOT.jar

# Run sequential code (without TornadoVM acceleration)
tornado qconlondon.MatrixMultiplication 512 seq

# Run with TornadoVM acceleration on the default device
tornado qconlondon.MatrixMultiplication 512 tornado

# Run with TornadoVM on the default device with debug information
tornado --debug qconlondon.MatrixMultiplication 512 tornado

# Run with TornadoVM and print the generated OpenCL kernel
tornado --printKernel qconlondon.MatrixMultiplication 512 tornado

# See all devices available
tornado --devices 

# Run with TornadoVM on the device 1
# Convention: -D<taskScheduleName>.<taskName>.device=0:<deviceID> 
tornado --printKernel --debug -Ds0.t0.device=0:1 qconlondon.MatrixMultiplication 512 tornado

# Run with TornadoVM on the device 3 
# Convention: -D<taskScheduleName>.<taskName>.device=0:<deviceID> 
tornado --printKernel --debug -Ds0.t0.device=0:3 qconlondon.MatrixMultiplication 512 tornado

# Run TornadoVM with IGV (Ideal Graph Visualizer) to see the compiled graphs 
tornado --igv qconlondon.MatrixMultiplication 512 
```


## 3) Node.js and TornadoVM

See install guidelines for running TornadoVM with GraalVM [here](https://github.com/beehive-lab/TornadoVM/blob/master/assembly/src/docs/10_INSTALL_WITH_GRAALVM.md)

```bash
$ cd nodejs
$ export BASE=/path/to/tornadovm-graal
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

## Access the sequential execution
http://172.17.0.2:3000/java
```

##### Running stand-alone?

```bash
$ bash node.sh server.js

## Access
http://127.0.0.1:3000/

## Access the sequential execution
http://127.0.0.1:3000/java
```

