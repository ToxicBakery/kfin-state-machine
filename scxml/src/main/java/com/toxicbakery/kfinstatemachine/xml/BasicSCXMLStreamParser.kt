package com.toxicbakery.kfinstatemachine.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.InputStream
import javax.xml.stream.XMLInputFactory

class BasicSCXMLStreamParser : StreamParser<XmlRoot> {

    override fun parseStream(inputStream: InputStream): XmlRoot =
            XMLInputFactory.newFactory()
                    .createXMLStreamReader(inputStream)
                    .use { reader -> XmlMapper().readValue(reader, XmlRoot::class.java) }

}
