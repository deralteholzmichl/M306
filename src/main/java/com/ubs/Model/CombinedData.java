package com.ubs.Model;

import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.sdat.ValidatedMeteredData;

import java.util.ArrayList;
import java.util.List;

public class CombinedData {
    ESLBillingData eslBillingData;

    ArrayList<ValidatedMeteredData> validatedMeteredData = new ArrayList<>();

    public ESLBillingData getEslBillingData() {
        return eslBillingData;
    }

    public void setEslBillingData(ESLBillingData eslBillingData) {
        this.eslBillingData = eslBillingData;
    }

    public ArrayList<ValidatedMeteredData> getValidatedMeteredData() {
        return validatedMeteredData;
    }

    public void setValidatedMeteredData(ArrayList<ValidatedMeteredData> validatedMeteredData) {
        this.validatedMeteredData = validatedMeteredData;
    }
}
