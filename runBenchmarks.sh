
tornado -Ds0.t0.device=0:0 -Xmx16g -Xms16g -jar target/benchmarks.jar  | tee JMH-NVIDIA.log
tornado -Ds0.t0.device=0:1 -Xmx16g -Xms16g -jar target/benchmarks.jar  | tee JMH-INTELCPU.log
tornado -Ds0.t0.device=0:3 -Xmx16g -Xms16g -jar target/benchmarks.jar  | tee JMH-HDGRAPHICS.log


