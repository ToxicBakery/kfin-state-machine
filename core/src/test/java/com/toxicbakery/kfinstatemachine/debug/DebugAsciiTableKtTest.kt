package com.toxicbakery.kfinstatemachine.debug

import org.junit.Assert.assertEquals
import org.junit.Test

class DebugAsciiTableKtTest {

    @Test
    fun printAsciiTableTest() {
        listOf("one", "two", "three")
                .let(::createAsciiTable)
                .also { assertEquals(EXPECTED_TABLE, it) }
    }

    companion object {
        private const val EXPECTED_TABLE: String = """+-------+
| one   |
+-------+
| two   |
| three |
+-------+"""
    }

}