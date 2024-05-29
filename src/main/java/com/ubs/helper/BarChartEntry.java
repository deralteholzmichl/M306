package com.ubs.helper;

public class BarChartEntry implements Comparable<BarChartEntry> {
    String name;
    double value;

    public BarChartEntry(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(BarChartEntry barChartEntry) {
        return Double.compare(this.value, barChartEntry.value);
    }
}
