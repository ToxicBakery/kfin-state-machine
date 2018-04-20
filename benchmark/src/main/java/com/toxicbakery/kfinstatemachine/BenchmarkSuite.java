package com.toxicbakery.kfinstatemachine;

import com.toxicbakery.kfinstatemachine.graph.DirectedGraph;
import com.toxicbakery.kfinstatemachine.graph.IDirectedGraph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Map;

public class BenchmarkSuite {

    public static void main(String... args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*")
                .warmupIterations(2)
                .warmupTime(TimeValue.seconds(5))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(10))
                .forks(1)
                .threads(100)
                .mode(Mode.Throughput)
                .resultFormat(ResultFormatType.JSON)
                .result("build/jmh-result.json")
                .shouldFailOnError(true)
                .build();

        new Runner(options).run();
    }

    @State(Scope.Thread)
    public static class MachineInstantiationBenchmarkClass {
        @Param({"1", "10", "100", "1000"})
        private int graphEdgeCount;

        private IDirectedGraph<TestFiniteState, TestTransition> directedGraph;
        private TestFiniteState lastNode;

        @Setup
        public void setup() {
            Map<TestFiniteState, Map<TestTransition, TestFiniteState>> edges =
                    GraphBuilderKt.createGraphEdges(graphEdgeCount);

            directedGraph = new DirectedGraph<>(edges);
            lastNode = new TestFiniteState("node_" + (graphEdgeCount + 1));
        }

        @Benchmark
        public void machineInstantiation(Blackhole blackhole) {
            blackhole.consume(
                    new StateMachine<>(
                            directedGraph,
                            // Use the last node as the entry to force scanning the entire graph (worst case scenario)
                            lastNode
                    )
            );
        }
    }

    @State(Scope.Thread)
    public static class GraphBenchmarkClass {
        @Param({"1", "10", "100", "1000"})
        private int graphEdgeCount;

        private Map<TestFiniteState, Map<TestTransition, TestFiniteState>> graphEdges;

        @Setup
        public void setup() {
            graphEdges = GraphBuilderKt.createGraphEdges(graphEdgeCount);
        }

        @Benchmark
        public void directedGraphInstantiation(Blackhole blackhole) {
            blackhole.consume(new DirectedGraph<>(graphEdges));
        }
    }

}
