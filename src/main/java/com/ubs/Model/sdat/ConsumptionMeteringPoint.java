package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class ConsumptionMeteringPoint {
    private String VSENationalID;

    @XmlElement(name="VSENationalID", namespace = "http://www.strom.ch")
    String getVSENationalID() {
        return VSENationalID;
    }

    public void setVSENationalID(String VSENationalID) {
        this.VSENationalID = VSENationalID;
    }
}
