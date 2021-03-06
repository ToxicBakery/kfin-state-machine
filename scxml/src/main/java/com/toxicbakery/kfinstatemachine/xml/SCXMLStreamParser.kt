package com.toxicbakery.kfinstatemachine.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.toxicbakery.kfinstatemachine.xml.model.XmlRoot
import java.io.InputStream
import javax.xml.stream.XMLInputFactory

class SCXMLStreamParser : StreamParser<XmlRoot> {

    override fun parseStream(inputStream: InputStream): XmlRoot =
            XMLInputFactory.newFactory()
                    .createXMLStreamReader(inputStream)
                    .use { reader -> XmlMapper().readValue(reader, XmlRoot::class.java) }

}
