package com.toxicbakery.kfinstatemachine.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.InputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

class StartChartParser {

    fun parseChart(input: InputStream): XmlRoot =
            XMLInputFactory.newFactory()
                    .createXMLStreamReader(input)
                    .use { reader -> XmlMapper().readValue(reader, XmlRoot::class.java) }

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