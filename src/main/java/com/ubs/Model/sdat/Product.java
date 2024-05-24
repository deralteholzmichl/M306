package com.ubs.Model.sdat;

import javafx.fxml.FXML;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

public class Product {
    private BigInteger ID;
    private String measureUnit;

    @XmlElement(name="ID", namespace = "http://www.strom.ch")
    public BigInteger getID() {
        return ID;
    }

    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    @XmlElement(name="MeasureUnit", namespace = "http://www.strom.ch")
    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
}
