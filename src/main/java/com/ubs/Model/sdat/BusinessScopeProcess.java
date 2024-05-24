package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;

public class BusinessScopeProcess {
    private DocumentBusinessReasonType businessReasonType;
    private String BusinessDomainType;
    private int BusinessSectorType;
    private ReportPeriod reportPeriod;
    private BusinessService businessService;

    @XmlElement(name = "BusinessReasonType", namespace = "http://www.strom.ch")
    public DocumentBusinessReasonType getBusinessReasonType() {
        return businessReasonType;
    }

    public void setBusinessReasonType(DocumentBusinessReasonType businessReasonType) {
        this.businessReasonType = businessReasonType;
    }
    @XmlElement(name = "BusinessDomainType", namespace = "http://www.strom.ch")
    public String getBusinessDomainType() {
        return BusinessDomainType;
    }

    public void setBusinessDomainType(String businessDomainType) {
        BusinessDomainType = businessDomainType;
    }

    @XmlElement(name = "BusinessSectorType", namespace = "http://www.strom.ch")
    public int getBusinessSectorType() {
        return BusinessSectorType;
    }

    public void setBusinessSectorType(int businessSectorType) {
        BusinessSectorType = businessSectorType;
    }

    @XmlElement(name = "ReportPeriod", namespace = "http://www.strom.ch")
    public ReportPeriod getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(ReportPeriod reportPeriod) {
        this.reportPeriod = reportPeriod;
    }
    @XmlElement(name = "BusinessService", namespace = "http://www.strom.ch")
    public BusinessService getBusinessService() {
        return businessService;
    }

    public void setBusinessService(BusinessService businessService) {
        this.businessService = businessService;
    }
}

