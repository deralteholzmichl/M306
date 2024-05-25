package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.sql.Time;
import java.util.ArrayList;

public class Meter {
    private String factoryNo;
    private String internalNo;
    private ArrayList<TimePeriod> timePeriod = new ArrayList<>();

    @XmlAttribute(name = "factoryNo")
    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }
    @XmlAttribute(name = "internalNo")
    public String getInternalNo() {
        return internalNo;
    }

    public void setInternalNo(String internalNo) {
        this.internalNo = internalNo;
    }
    @XmlElement(name = "TimePeriod")
    public ArrayList<TimePeriod> getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(ArrayList<TimePeriod> timePeriod) {
        this.timePeriod = timePeriod;
    }
}
