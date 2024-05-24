package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class Receiver {
    private String role;
    private ID receiver;

    @XmlElement(name = "Role", namespace = "http://www.strom.ch")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @XmlElement(name = "ID", namespace = "http://www.strom.ch")
    public ID getReceiver() {
        return receiver;
    }

    public void setReceiver(ID receiver) {
        this.receiver = receiver;
    }
}
