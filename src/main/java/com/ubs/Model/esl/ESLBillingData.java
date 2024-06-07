package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@XmlRootElement(name = "ESLBillingData")
public class ESLBillingData {
    private Header Header;
    private ArrayList<Meter> Meter = new ArrayList<>();

    @XmlElement(name = "Header")
    public Header getHeader() {
        return Header;
    }

    public void setHeader(Header header) {
        Header = header;
    }
    @XmlElement(name = "Meter")
    public ArrayList<Meter> getMeter() {
        return Meter;
    }

    public void setMeter(ArrayList<Meter> meter) {
        Meter = meter;
    }

    public int compareTo(String otherDate) {
        LocalDateTime OtherLocalDateTime = LocalDateTime.parse(otherDate);
        LocalDateTime ThisLocalDateTime = LocalDateTime.parse(this.Header.getCreated());
        // Zuerst die Daten vergleichen
        int dateComparison = ThisLocalDateTime.compareTo(OtherLocalDateTime);
        if (dateComparison != 0) {
            return dateComparison;
        }
        // Wenn die Daten gleich sind, die Zeiten vergleichen
        return ThisLocalDateTime.compareTo(OtherLocalDateTime);
    }


}
