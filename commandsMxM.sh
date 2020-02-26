#!/usr/bin/env bash

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
