# Dungeon

This is a small MUD style demonstration using the state machine implementation as a path logic validator.
Maps are generated via a tunnel algorithm then converted to a state machine representing all possible paths in the map.
The player is spawned at a random location in one of the map tunnels and can begin exploring the 'dungeon'.

## Building

The sample is intended to be run as a command line application and is built with gradle.

`./gradlew :samples:dungeon:build`

## Running

The sample is build as a distribution and can be run once unpacked.

Distributions  
`samples/dungeon/build/distributions/`

Run the application  
`./bin/dungeon`