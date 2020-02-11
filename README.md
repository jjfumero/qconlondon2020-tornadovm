#TornadoVM QCon-London demos


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

