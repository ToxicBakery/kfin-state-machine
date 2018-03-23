package com.toxicbakery.kfinstatemachine.xml.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "scxml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlRoot extends XmlState {
    @JacksonXmlProperty(isAttribute = true)
    public String initial;
}
