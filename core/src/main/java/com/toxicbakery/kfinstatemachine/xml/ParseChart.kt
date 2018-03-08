package com.toxicbakery.kfinstatemachine.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.InputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

class ParseChart {

    fun parseChart(input: InputStream) =
            XMLInputFactory.newFactory()
                    .createXMLStreamReader(input)
                    .use { reader -> XmlMapper().readValue(reader, XmlRoot::class.java) }
                    .let {
                        val stateIds: Set<String> = (it.states ?: listOf())
                                .map { it.id }
                                .toSet()

                        val events: Set<String> = (it.states ?: listOf())
                                .flatMap { it.transitions.map { it.event } }
                                .toSet()

                        return@let Pair(stateIds, events)
                    }

}

internal fun parseRoot(xmlRoot: XmlRoot) {
    val states: Set<String> = (xmlRoot.states ?: listOf())
            .map { it.id }
            .toSet()

    val events: Set<String> = (xmlRoot.states ?: listOf())
            .flatMap { it.transitions.map { it.event } }
            .toSet()

    val parallels: Set<String> = (xmlRoot.parallel ?: listOf())
            .map { it.id }
            .toSet()
}

/**
 * Use implementation for XMLStreamReader wrapping close functionality.
 */
internal inline fun <T : XMLStreamReader?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        when {
            this == null -> Unit
            else -> close()
        }
    }
}