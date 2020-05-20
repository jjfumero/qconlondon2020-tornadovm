/*
 * Copyright (c) 2020, APT Group, Department of Computer Science,
 * School of Engineering, The University of Manchester. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package qconlondon;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import uk.ac.manchester.tornado.api.TaskSchedule;
import uk.ac.manchester.tornado.api.annotations.Parallel;

public class MatrixMultiplicationJMH {

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

    @State(Scope.Thread)
    public static class SequentialCodeSetup {
        @Param({ "256", "512", "1024", "2048" })
        public int size;

        @Setup(Level.Trial)
        public void doSetup() {
            matrixA = new float[size * size];
            matrixB = new float[size * size];
            matrixC = new float[size * size];
            Random r = new Random();
            IntStream.range(0, size * size).parallel().forEach(idx -> {
                matrixA[idx] = r.nextFloat();
                matrixB[idx] = r.nextFloat();
            });
        }

        public float[] matrixA;
        public float[] matrixB;
        public float[] matrixC;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 60, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 30, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(1)
    public void javaMxM(SequentialCodeSetup state) {
        matrixMultiplication(state.matrixA, state.matrixB, state.matrixC, state.size);
    }

    @State(Scope.Thread)
    public static class TornadoVMCodeSetup {
        @Param({ "256", "512", "1024", "2048" })
        public int size;

        @Setup(Level.Trial)
        public void doSetup() {
            matrixA = new float[size * size];
            matrixB = new float[size * size];
            matrixC = new float[size * size];
            Random r = new Random();
            IntStream.range(0, size * size).parallel().forEach(idx -> {
                matrixA[idx] = r.nextFloat();
                matrixB[idx] = r.nextFloat();
            });

            t = new TaskSchedule("s0") //
                    .streamIn(matrixA, matrixB) //
                    .task("t0", MatrixMultiplicationJMH::matrixMultiplication, matrixA, matrixB, matrixC, size) //
                    .streamOut(matrixC); //
            t.warmup();
        }

        public TaskSchedule t;
        public float[] matrixA;
        public float[] matrixB;
        public float[] matrixC;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 30, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 30, timeUnit = TimeUnit.SECONDS)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Fork(1)
    public void tornadoMxM(TornadoVMCodeSetup state, Blackhole blackhole) {
        TaskSchedule t = state.t;
        t.execute();
        blackhole.consume(t);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder() //
                .include(MatrixMultiplicationJMH.class.getSimpleName()) //
                .warmupIterations(2) //
                .measurementIterations(5) //
                .forks(1) //
                .jvmArgs("-server", "-Xmx16g", "-Xms16g") //
                .build();
        new Runner(opt).run();

    }

}