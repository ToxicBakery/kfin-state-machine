# Benchmark
This module exists to simplify testing implementation changes to understand and validate relative impact.
As benchmarking is highly dependant on the host machine, benchmarks are only intended to be used run on developer machines.

## Run Benchmarks
Benchmarks can be run via gradle.

`./gradlew runJMH`

JMH reporting will be generated automatically and can be found in the build output.

`build/jmh-report/index.html`