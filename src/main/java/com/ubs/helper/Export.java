package com.ubs.helper;

import com.ubs.Model.CombinedData;
import com.ubs.Model.Data;
import com.ubs.Model.esl.ValueRow;
import com.ubs.Model.sdat.Observation;
import com.ubs.Model.sdat.ValidatedMeteredData;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Export {


    public void exportToCSV(List<CombinedData> combinedData,String fileArt) {
        String sensorData = "";
        List<Data> datas = new ArrayList<>();
        if (fileArt.equals("Verbrauch")) {
            sensorData = ("ID742");
        }else {
            sensorData = ("ID735");

        }
        for (CombinedData data : combinedData) {
            LocalDateTime timestamp = LocalDateTime.parse(data.getEslBillingData().getMeter().getFirst().getTimePeriod().getFirst().getEnd());
            Double stand = 0.0;
            for (ValueRow vr : data.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                if (fileArt.equals("Verbrauch")) {
                    if (vr.getObis().equals("1-1:1.8.1")) {
                        stand = Double.valueOf(vr.getValue());
                        break;
                    }
                }else{
                    if (vr.getObis().equals("1-1:1.8.2")) {
                        stand = Double.valueOf(vr.getValue());
                        break;
                    }
                }
            }
            for (ValueRow vr : data.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                if (fileArt.equals("Einspeisen")){
                    if (vr.getObis().equals("1-1:2.8.1")) {
                        stand += Double.valueOf(vr.getValue());
                        break;
                    }
                }else{
                    if (vr.getObis().equals("1-1:2.8.2")) {
                        stand += Double.valueOf(vr.getValue());
                        break;
                    }
                }
            }
            datas.add(new Data(timestamp.toString(),String.valueOf(stand)));
            for (ValidatedMeteredData meteredData : data.getValidatedMeteredData()) {
                for (Observation observation : meteredData.getMeteringData().getObservations()) {
                    timestamp = timestamp.plusMinutes(15);
                    stand += observation.getVolume();
                    datas.add(new Data(timestamp.toString(),String.valueOf(stand)));
                }
            }
        }

        try (FileWriter writer = new FileWriter("C:\\Users\\leoni\\Downloads\\"+sensorData+".csv")) {
            // Schreibe die Kopfzeile
            writer.append("Timestamp,Value\n");

            // Schreibe die Datenobjekte
            for (Data data : datas) {
                writer.append(data.getTimestamp()).append(",");
                writer.append(data.getValue()).append("\n");
            }

            System.out.println("CSV-Datei erfolgreich erstellt.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
