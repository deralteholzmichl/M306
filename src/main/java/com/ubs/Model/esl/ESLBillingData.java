package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "ESLBillingData")
public class ESLBillingData {
    private Header Header;
    private ArrayList<Meter> Meter = new ArrayList<>();

    @XmlElement(name = "Header")
    public Header getHeader() {
        return Header;
    }

    public void setHeader(Header header) {
        Header = header;
    }
    @XmlElement(name = "Meter")
    public ArrayList<Meter> getMeter() {
        return Meter;
    }

    public void setMeter(ArrayList<Meter> meter) {
        Meter = meter;
    }
}
