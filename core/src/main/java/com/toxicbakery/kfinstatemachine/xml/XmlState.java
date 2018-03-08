package com.toxicbakery.kfinstatemachine.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class XmlState {
    @JacksonXmlProperty(isAttribute = true)
    public String id;

    @JacksonXmlProperty(localName = "initial")
    public XmlInitial initial;

    @JacksonXmlProperty(localName = "transition")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<XmlTransition> transitions;

    @JacksonXmlProperty(localName = "state")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<XmlState> states;

    @JacksonXmlProperty(localName = "parallel")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<XmlState> parallel;
}
