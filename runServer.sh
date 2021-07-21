#!/bin/bash

echo "tornado --printKernel --threadInfo -cp target/tornado-1.0-SNAPSHOT.jar qconlondon.Server "
tornado --threadInfo --printBytecodes -cp target/tornado-1.0-SNAPSHOT.jar qconlondon.Server 

