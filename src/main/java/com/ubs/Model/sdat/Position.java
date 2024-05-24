package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class Position {
    public int getSequence() {
        return Sequence;
    }

    public void setSequence(int sequence) {
        Sequence = sequence;
    }

    @XmlElement(name="Sequence", namespace = "http://www.strom.ch")
    private int Sequence;
}
