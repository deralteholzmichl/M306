package com.ubs.Model;

public class Data {
    String Timestamp;
    String value;

    public Data(String timestamp, String value) {
        Timestamp = timestamp;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
