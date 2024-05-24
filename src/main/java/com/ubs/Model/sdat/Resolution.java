package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class Resolution {
    private int resolution;
    private String Unit;

    @XmlElement(name="Resolution", namespace = "http://www.strom.ch")
    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    @XmlElement(name="Unit", namespace = "http://www.strom.ch")
    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }
}
