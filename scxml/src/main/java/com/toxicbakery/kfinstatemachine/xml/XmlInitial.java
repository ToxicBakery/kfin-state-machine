package com.toxicbakery.kfinstatemachine.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlInitial {

    @JacksonXmlProperty
    public XmlTransition transition;
}
