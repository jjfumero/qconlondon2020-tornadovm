package qconlondon;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.IntStream;

import uk.ac.manchester.tornado.api.TaskSchedule;
import uk.ac.manchester.tornado.api.annotations.Parallel;

public class MatrixMultiplication {

    private static final int MAX_ITERATIONS = 100;

    private static void matrixMultiplication(final float[] A, final float[] B, final float[] C, final int size) {
        for (@Parallel int i = 0; i < size; i++) {
            for (@Parallel int j = 0; j < size; j++) {
                float sum = 0.0f;
                for (int k = 0; k < size; k++) {
                    sum += A[(i * size) + k] * B[(k * size) + j];
                }
                C[(i * size) + j] = sum;
            }
        }
    }

    public static void main(String[] args) {

        String executionType = "tornado";
        int size = 512;
        if (args.length >= 1) {
            size = Integer.parseInt(args[0]);

            if (args.length >= 2) {
                executionType = args[1];
            }
        }

        System.out.println("Computing MxM of " + size + "x" + size);

        float[] matrixA = new float[size * size];
        float[] matrixB = new float[size * size];
        float[] matrixC = new float[size * size];
        float[] resultSeq = new float[size * size];

        Random r = new Random();
        IntStream.range(0, size * size).parallel().forEach(idx -> {
            matrixA[idx] = r.nextFloat();
            matrixB[idx] = r.nextFloat();
        });

        //@formatter:off
        TaskSchedule t = new TaskSchedule("s0")
                .task("t0",MatrixMultiplication::matrixMultiplication,matrixA,matrixB,matrixC,size)
                .streamOut(matrixC);
        //@formatter:on

        if ("tornado".equals(executionType)) {
            runTornado(t);
        } else {
            runSequential(matrixA, matrixB, resultSeq, size);
        }
    }

    private static void runSequential(final float[] A, final float[] B, final float[] C, final int size) {
        DecimalFormat df = new DecimalFormat("0.00##");
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            long startSequential = System.nanoTime();
            matrixMultiplication(A, B, C, size);
            long endSequential = System.nanoTime();
            long totalTime = (endSequential - startSequential);
            System.out.println("Total time: " + totalTime + " (ns), " + (df.format(totalTime * 1E-9)) + " (s)");
        }
    }

    private static void runTornado(TaskSchedule t) {
        DecimalFormat df = new DecimalFormat("0.00##");
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            long start = System.nanoTime();
            t.execute();
            long end = System.nanoTime();
            long totalTime = (end - start);
            System.out.println("Total time: " + totalTime + " (ns), " + (df.format(totalTime * 1E-9)) + "(s)");
        }
    }
}