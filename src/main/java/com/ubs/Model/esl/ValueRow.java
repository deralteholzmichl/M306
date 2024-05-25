package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlAttribute;

public class ValueRow {
    private String obis;
    private String valueTimeStamp;
    private String value;
    private String status;

    @XmlAttribute(name = "obis")
    public String getObis() {
        return obis;
    }

    public void setObis(String obis) {
        this.obis = obis;
    }
    @XmlAttribute(name = "valueTimeStamp")
    public String getValueTimeStamp() {
        return valueTimeStamp;
    }

    public void setValueTimeStamp(String valueTimeStamp) {
        this.valueTimeStamp = valueTimeStamp;
    }
    @XmlAttribute(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @XmlAttribute(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
