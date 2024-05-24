package com.ubs.Model.sdat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
public class Interval {

    private String startDateTime;
    private String endDateTime;

    @XmlElement(name="StartDateTime", namespace = "http://www.strom.ch")
    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    @XmlElement(name="EndDateTime", namespace = "http://www.strom.ch")
    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
