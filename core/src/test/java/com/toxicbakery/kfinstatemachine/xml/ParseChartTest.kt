package com.toxicbakery.kfinstatemachine.xml

import org.junit.Test

class ParseChartTest {

    @Test
    fun parseStopWatch() {
        ParseChart::class.java.getResource("stopwatch.xml")
                .openStream()
                .let(ParseChart()::parseChart)
                .let { println(it) }
    }

    @Test
    fun parseMicrowave() {
        ParseChart::class.java.getResource("microwave.xml")
                .openStream()
                .let(ParseChart()::parseChart)
                .let { println(it) }
    }

}