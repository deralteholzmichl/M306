package com.ubs.Model.esl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

public class TimePeriod {
    private String end;
    private ArrayList<ValueRow> valueRow = new ArrayList<>();

    @XmlAttribute(name = "end")
    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    @XmlElement(name = "ValueRow")
    public ArrayList<ValueRow> getValueRow() {
        return valueRow;
    }

    public void setValueRow(ArrayList<ValueRow> valueRow) {
        this.valueRow = valueRow;
    }
}
