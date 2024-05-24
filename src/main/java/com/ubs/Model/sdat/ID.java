package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class ID {
    private String EICID;
    @XmlElement(name = "EICID", namespace = "http://www.strom.ch")
    public String getEICID() {
        return EICID;
    }

    public void setEICID(String EICID) {
        this.EICID = EICID;
    }
}
