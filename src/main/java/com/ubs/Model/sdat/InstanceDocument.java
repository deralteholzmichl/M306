package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class InstanceDocument {
    private int dictionaryAgencyID;
    private String versionID;
    private String documentID;
    private DocumentBusinessReasonType documentType;
    private String creation;
    private int status;

    @XmlElement(name = "DictionaryAgencyID", namespace = "http://www.strom.ch")
    public int getDictionaryAgencyID() {
        return dictionaryAgencyID;
    }

    public void setDictionaryAgencyID(int dictionaryAgencyID) {
        this.dictionaryAgencyID = dictionaryAgencyID;
    }
    @XmlElement(name = "VersionID", namespace = "http://www.strom.ch")
    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }
    @XmlElement(name = "DocumentID", namespace = "http://www.strom.ch")
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @XmlElement(name = "DocumentType", namespace = "http://www.strom.ch")
    public DocumentBusinessReasonType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentBusinessReasonType documentType) {
        this.documentType = documentType;
    }

    @XmlElement(name = "Creation", namespace = "http://www.strom.ch")
    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    @XmlElement(name = "Status", namespace = "http://www.strom.ch")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
