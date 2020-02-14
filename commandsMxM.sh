

tornado qconlondon.MatrixMultiplication 512 seq

tornado qconlondon.MatrixMultiplication 512 tornado

tornado --debug qconlondon.MatrixMultiplication 512 tornado

tornado --printKernel qconlondon.MatrixMultiplication 512 tornado

tornado --printKernel --debug -Ds0.t0.device=0:1 qconlondon.MatrixMultiplication 512 tornado

tornado --printKernel --debug -Ds0.t0.device=0:3 qconlondon.MatrixMultiplication 512 tornado

