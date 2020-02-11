# TornadoVM QCon-London 2020 - Demos


#### 1) Live Task Migration


```bash
$ export JAVA_HOME=/PATH/TO/graal-jvmci-8/jdk1.8.0/product
$ export TORNADO_ROOT=/PATH/TO/TORNADOVM/ROOT
$ export PATH="${PATH}:${TORNADO_ROOT}/bin/bin/"
$ export TORNADO_SDK=${TORNADO_ROOT}/bin/sdk

## Compile
$ mvn clean package
```

How to reproduce:

```bash
./runServer.sh

## In another terminal
./runClient.sh
```



#### 2) Matrix Multiplication


```bash
$ export CLASSPATH=target/tornado-1.0-SNAPSHOT.jar
$ tornado qconlondon.MatrixMultiplication
$ tornado qconlondon.MatrixMultiplication 512 seq
$ tornado qconlondon.MatrixMultiplication 512 tornado
$ tornado --debug qconlondon.MatrixMultiplication 512 tornado
$ tornado --printKernel qconlondon.MatrixMultiplication 512 tornado
$ tornado --printKernel --debug -Ds0.t0.device=0:1 qconlondon.MatrixMultiplication 512 tornado
```


#### 3) Node.js and TornadoVM


