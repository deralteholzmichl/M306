package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Header {
    private String version;
    private String created;
    private String swSystemNameFrom;
    private String swSystemNameTo;
    @XmlAttribute(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    @XmlAttribute(name = "created")
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    @XmlAttribute(name = "swSystemNameFrom")
    public String getSwSystemNameFrom() {
        return swSystemNameFrom;
    }

    public void setSwSystemNameFrom(String swSystemNameFrom) {
        this.swSystemNameFrom = swSystemNameFrom;
    }
    @XmlAttribute(name = "swSystemNameTo")
    public String getSwSystemNameTo() {
        return swSystemNameTo;
    }

    public void setSwSystemNameTo(String swSystemNameTo) {
        this.swSystemNameTo = swSystemNameTo;
    }
}
