package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class DocumentBusinessReasonType {
    private String ebIXCode;

    @XmlElement(name = "ebIXCode", namespace = "http://www.strom.ch")
    public String getEbIXCode() {
        return ebIXCode;
    }

    public void setEbIXCode(String ebIXCode) {
        this.ebIXCode = ebIXCode;
    }
}
