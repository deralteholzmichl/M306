package com.ubs.Model.sdat;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
public class ValidatedMeteredData_HeaderInformation {
    private String headerVersion;
    private Sender sender;
    private Receiver receiver;
    private InstanceDocument instanceDocument;
    private BusinessScopeProcess businessScopeProcess;

    @XmlElement(name = "HeaderVersion", namespace = "http://www.strom.ch")
    public String getHeaderVersion() {
        return headerVersion;
    }

    public void setHeaderVersion(String headerVersion) {
        this.headerVersion = headerVersion;
    }
    @XmlElement(name = "Sender", namespace = "http://www.strom.ch")
    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }
    @XmlElement(name = "Receiver", namespace = "http://www.strom.ch")
    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @XmlElement(name = "InstanceDocument", namespace = "http://www.strom.ch")
    public InstanceDocument getInstanceDocument() {
        return instanceDocument;
    }

    public void setInstanceDocument(InstanceDocument instanceDocument) {
        this.instanceDocument = instanceDocument;
    }

    @XmlElement(name = "BusinessScopeProcess", namespace = "http://www.strom.ch")
    public BusinessScopeProcess getBusinessScopeProcess() {
        return businessScopeProcess;
    }

    public void setBusinessScopeProcess(BusinessScopeProcess businessScopeProcess) {
        this.businessScopeProcess = businessScopeProcess;
    }
}
