package com.ubs.Model.sdat;
import javax.xml.bind.annotation.*;

@XmlRootElement(namespace = "http://www.strom.ch", name = "ValidatedMeteredData_13")
public class ValidatedMeteredData_13 implements ValidatedMeteredData{
    private MeteringData MeteringData;
    private ValidatedMeteredData_HeaderInformation ValidatedMeteredData_HeaderInformation;

    @XmlElement(name = "ValidatedMeteredData_HeaderInformation", namespace = "http://www.strom.ch")
    public ValidatedMeteredData_HeaderInformation getValidatedMeteredData_HeaderInformation() {
        return ValidatedMeteredData_HeaderInformation;
    }
    public void setValidatedMeteredData_HeaderInformation(ValidatedMeteredData_HeaderInformation validatedMeteredData_HeaderInformation) {
        ValidatedMeteredData_HeaderInformation = validatedMeteredData_HeaderInformation;
    }
    @XmlElement(name = "MeteringData", namespace = "http://www.strom.ch")
    public MeteringData getMeteringData() {
        return MeteringData;
    }
    public void setMeteringData(MeteringData meteringData) {
        MeteringData = meteringData;
    }
}
