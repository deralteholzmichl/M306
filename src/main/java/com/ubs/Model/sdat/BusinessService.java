package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class BusinessService {

    private ServiceTransaction serviceTransaction;

    @XmlElement(name = "ServiceTransaction", namespace = "http://www.strom.ch")
    public ServiceTransaction getServiceTransaction() {
        return serviceTransaction;
    }

    public void setServiceTransaction(ServiceTransaction serviceTransaction) {
        this.serviceTransaction = serviceTransaction;
    };
}
