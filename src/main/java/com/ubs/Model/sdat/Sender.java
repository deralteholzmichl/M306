package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class Sender {
    private String role;

    private ID sender;

    @XmlElement(name = "Role", namespace = "http://www.strom.ch")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @XmlElement(name = "ID", namespace = "http://www.strom.ch")
    public ID getSender() {
        return sender;
    }

    public void setSender(ID sender) {
        this.sender = sender;
    }
}
