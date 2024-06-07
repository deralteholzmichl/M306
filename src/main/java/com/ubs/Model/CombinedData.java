package com.ubs.Model;

import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.sdat.ValidatedMeteredData;

public class CombinedData {
    ESLBillingData eslBillingData;

    ValidatedMeteredData validatedMeteredData;

    public ESLBillingData getEslBillingData() {
        return eslBillingData;
    }

    public void setEslBillingData(ESLBillingData eslBillingData) {
        this.eslBillingData = eslBillingData;
    }

    public ValidatedMeteredData getValidatedMeteredData() {
        return validatedMeteredData;
    }

    public void setValidatedMeteredData(ValidatedMeteredData validatedMeteredData) {
        this.validatedMeteredData = validatedMeteredData;
    }
}
