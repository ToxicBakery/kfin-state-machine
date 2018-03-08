package com.toxicbakery.kfinstatemachine.xml

import org.junit.Test

class StartChartParserTest {

    @Test
    fun parseStopWatch() {
        StartChartParser::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(StartChartParser()::parseChart)
                .let { println(it) }
    }

    @Test
    fun parseMicrowave() {
        StartChartParser::class.java.getResource("microwave.xml")
                .openStream()
                .let(StartChartParser()::parseChart)
                .let { println(it) }
    }

}