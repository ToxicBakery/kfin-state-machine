package com.toxicbakery.kfinstatemachine.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlTransition {
    @JacksonXmlProperty(isAttribute = true)
    public String event;

    @JacksonXmlProperty(isAttribute = true)
    public String target;
}
