package com.ubs.Model.sdat;

import javafx.geometry.Pos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Observation {
    private Position position;

    private double volume;

    @XmlElement(name="Position", namespace = "http://www.strom.ch")
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @XmlElement(name="Volume", namespace = "http://www.strom.ch")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
