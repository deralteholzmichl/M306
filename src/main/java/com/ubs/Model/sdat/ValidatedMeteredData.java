package com.ubs.Model.sdat;

import java.time.LocalDateTime;

public interface ValidatedMeteredData {
    MeteringData getMeteringData();

    ValidatedMeteredData_HeaderInformation getValidatedMeteredData_HeaderInformation();

    int compareTo(String otherDate);
}
