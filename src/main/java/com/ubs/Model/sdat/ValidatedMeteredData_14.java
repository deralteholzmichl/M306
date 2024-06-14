package com.ubs.Model.sdat;
import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@XmlRootElement(namespace = "http://www.strom.ch", name = "ValidatedMeteredData_14")
public class ValidatedMeteredData_14 implements ValidatedMeteredData{
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
    public int compareTo(String otherDate) {
        ZonedDateTime OtherZonedDateTime = ZonedDateTime.parse(otherDate);
        ZonedDateTime ThisZonedDateTime = ZonedDateTime.parse(this.getMeteringData().getInterval().getEndDateTime());

        int dateComparison = ThisZonedDateTime.compareTo(OtherZonedDateTime);
        if (dateComparison != 0) {
            return dateComparison;
        }

        return ThisZonedDateTime.compareTo(OtherZonedDateTime);
    }
}
