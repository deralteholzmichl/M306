package com.ubs.helper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticDrawer {
    //MAX_HEIGHT: Wie hoch die Grafik sein soll
    //MAX_WIDTH: Wie breit die Grafik sein soll
    //PADDING: Abstand vom Rand zur Grafik (links/unten) für Beschriftung -> Mindestens 30 für Beschriftung
    //BESCHRIFTUNGX: Ob die X-Achse beschriftet werden soll
    //BESCHRIFTUNGY: Ob die Y-Achse beschriftet werden soll
    //DatenBeschrX: Beschriftungen der X-Achse
    public static Canvas drawLineDiagram(List<List<Double>> list, double MAX_HEIGHT, double MAX_WIDTH, double PADDING, boolean BESCHRIFTUNGX, boolean BESCHRIFTUNGY, List<String> DatenBeschrX, Interval interval) {

        List<Double> verbrauchList = list.get(0);
        List<Double> einspeisenList = list.get(1);

        // Berechne die erforderliche Gesamthöhe, beginnend bei 0
        double requiredHeightVerbrauch = 0;
            requiredHeightVerbrauch += verbrauchList.getLast()-verbrauchList.getFirst();
        double requiredHeightEinspeisen = 0;
            requiredHeightEinspeisen = einspeisenList.getLast()-einspeisenList.getFirst();


        double requiredHeight = Math.max(requiredHeightVerbrauch, requiredHeightEinspeisen);
        if (requiredHeight == 0) {
            return null;
        }
        double requiredHeightIndex = (MAX_HEIGHT / requiredHeight);


        // Berechne den Höhenindex, um die Höhen der Linien zu skalieren
        double heightIndexVerbrauch = ((MAX_HEIGHT) / requiredHeightVerbrauch);
        ArrayList<Double> indexedListHeightVerbrauch = new ArrayList<>();
        for (double i : verbrauchList) {
            double wert = (i-verbrauchList.getFirst()) * heightIndexVerbrauch;
            if (!(wert < 0) && ((wert) < MAX_HEIGHT + 10)){
                indexedListHeightVerbrauch.add(wert);
            }
        }
        double heightIndexEinspeisen = ((MAX_HEIGHT) / requiredHeightEinspeisen);
        ArrayList<Double> indexedListHeightEinspeisen = new ArrayList<>();
        for (double i : einspeisenList) {
            double wert = (i-einspeisenList.getFirst()) * heightIndexEinspeisen;
            if (!(wert < 0) && ((wert) < MAX_HEIGHT + 10)){
                indexedListHeightEinspeisen.add(wert);
            }
        }

        // Berechne die Breite pro Intervall basierend auf der Anzahl der Elemente in der Liste
        double WIDTH_PER_INTERVAL_EINSPEISEN = MAX_WIDTH / (einspeisenList.size()- 1);
        double WIDTH_PER_INTERVAL_VERBRAUCH = MAX_WIDTH / (verbrauchList.size()- 1);
        // Erstelle eine neue Canvas mit den angegebenen Breiten und Höhen plus Padding
        Canvas canvas = new Canvas(MAX_WIDTH + PADDING + 40, MAX_HEIGHT + PADDING + 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Setze die Startposition der Höhe unter Berücksichtigung des Paddings und der ersten Höhe in der skalierten Liste
        double height = canvas.getHeight() - PADDING - indexedListHeightVerbrauch.getFirst();
        double width = PADDING;
        gc.fillOval(width - 3, height - 3, 5, 5);
        //double newHeight = height;
        gc.setFont(Font.font(30));
        // Zeichne die Linien und Punkte basierend auf den skalierten Höhen und Breiten für Verbrauch
        for (int i = 1; i < indexedListHeightVerbrauch.size(); i++) {
            // Berechne die neuen Koordinaten
            double newHeight = height - indexedListHeightVerbrauch.get(i);
            double previousHeight = height - indexedListHeightVerbrauch.get(i - 1);

            // Zeichne eine Linie vom vorherigen Punkt zum aktuellen Punkt
            gc.strokeLine(width, previousHeight, width + WIDTH_PER_INTERVAL_VERBRAUCH, newHeight);

            // Aktualisiere die Breite für den nächsten Punkt
            width += WIDTH_PER_INTERVAL_VERBRAUCH;

            // Zeichne einen Punkt am aktuellen Punkt
           // gc.fillOval(width - 3, newHeight - 3, 5, 5);
        }

        height = canvas.getHeight() - PADDING;
        width = PADDING;
        gc.setStroke(Color.GREEN);
        gc.fillOval(width - 3, height - indexedListHeightEinspeisen.getFirst() - 3, 5, 5);

        for (int i = 1; i < indexedListHeightEinspeisen.size(); i++) {
            // Berechne die neuen Koordinaten
            double newHeight = height - indexedListHeightEinspeisen.get(i);
            double previousHeight = height - indexedListHeightEinspeisen.get(i - 1);

            // Zeichne eine Linie vom vorherigen Punkt zum aktuellen Punkt
            gc.strokeLine(width, previousHeight, width + WIDTH_PER_INTERVAL_EINSPEISEN, newHeight);

            // Aktualisiere die Breite für den nächsten Punkt
            width += WIDTH_PER_INTERVAL_EINSPEISEN;
        }
        gc.setFont(Font.font(8));
        gc.setStroke(Color.BLACK);
        // Anzahl der Unterteilungen für die X- und Y-Achsenbeschriftungen
        int UNTERTEILUNGENY = 10;

        // Passe die Schriftgröße an, wenn zu viele Unterteilungen vorhanden sind
        if (UNTERTEILUNGENY > 10) {
            gc.setFont(Font.font(10));
        }
        double hoeheY = 0.0;
        // Zeichne Beschriftungen auf der Y-Achse
        if (BESCHRIFTUNGY) {
            String einsesen = String.format("%.1f",einspeisenList.getFirst());
            String verbrauch = String.format("%.1f",verbrauchList.getFirst());

            double x =PADDING / 5;
            double y = canvas.getHeight() - PADDING;
            //nullpunkt
            gc.setFill(Color.GREEN);
            gc.fillText(einsesen, x, y);

            gc.setFill(Color.BLACK);
            x += gc.getFont().getSize() * 4;

            gc.fillText("/"+verbrauch, x, y);


            for (int i = 1; i < UNTERTEILUNGENY + 1; i++) {
                // Zeichne Markierungen und Beschriftungen auf der Y-Achse
                gc.strokeLine(PADDING - 2, canvas.getHeight() - PADDING - ((requiredHeight * requiredHeightIndex / UNTERTEILUNGENY) * i), PADDING + 2, canvas.getHeight() - PADDING - ((requiredHeight * requiredHeightIndex / UNTERTEILUNGENY) * i));

                einsesen = String.format("%.1f",einspeisenList.getFirst()+ ((requiredHeightEinspeisen / UNTERTEILUNGENY) * i));

                verbrauch = String.format("%.1f",verbrauchList.getFirst()+ ((requiredHeightVerbrauch / UNTERTEILUNGENY) * i));

                x = PADDING / 5;
                y = canvas.getHeight() - PADDING - ((requiredHeight * requiredHeightIndex / UNTERTEILUNGENY) * i) + 5;

                gc.setFill(Color.GREEN);
                gc.fillText(einsesen, x, y);

                gc.setFill(Color.BLACK);
                x += gc.getFont().getSize() * 4;

                gc.fillText("/"+verbrauch, x, y);

              //  gc.fillText(String.valueOf(textFlow), PADDING / 5, canvas.getHeight() - PADDING - ((requiredHeight * requiredHeightIndex / UNTERTEILUNGENY) * i) + 5);
                hoeheY = canvas.getHeight() - PADDING - ((requiredHeight * requiredHeightIndex / UNTERTEILUNGENY) * i);
            }
        }

        // Zeichne Beschriftungen auf der X-Achse
        if (BESCHRIFTUNGX) {
            int index = 0;
            for (String s:DatenBeschrX){
                if (!s.isEmpty()){
                    index++;
                    System.out.println(s);
                }
            }
            int UNTERTEILUNGENX = index-1;

            if (UNTERTEILUNGENX > 10) {
                gc.setFont(Font.font(10));
            }
            // Nullpunkt
           // gc.fillText(DatenBeschrX.get(0), PADDING, canvas.getHeight() - PADDING / UNTERTEILUNGENX);
            for (int i = 0; i <= UNTERTEILUNGENX; i++) {
                gc.setFont(Font.font(8));
                // Zeichne Markierungen und Beschriftungen auf der X-Achse
                if (i == 0){
                    gc.fillText(DatenBeschrX.get(i), PADDING-20, canvas.getHeight() - (PADDING - 30));
                }else {
                    if (!DatenBeschrX.get(i).isEmpty()) {
                        gc.strokeLine(((MAX_WIDTH / UNTERTEILUNGENX) * i) + PADDING, (canvas.getHeight() - PADDING - 2), ((MAX_WIDTH / UNTERTEILUNGENX) * i) + PADDING, (canvas.getHeight() - PADDING + 2));
                        gc.fillText(DatenBeschrX.get(i), ((MAX_WIDTH / UNTERTEILUNGENX) * i) + PADDING - 10, canvas.getHeight() - (PADDING - 30));
                    }
                }
            }
        }

        // Zeichne die X-Achse
        gc.strokeLine(PADDING, canvas.getHeight() - PADDING, MAX_WIDTH+PADDING, canvas.getHeight() - PADDING);
        // Zeichne die Y-Achse
        gc.strokeLine(PADDING, canvas.getHeight() - PADDING, PADDING, hoeheY);

        return canvas;
    }
    public static Canvas drawBarChart(double WIDTH, double HEIGHT, double PADDING, List<List<BarChartEntry>> entryList, Interval interval) {
        // Berechnung der maximalen Breite für die Balken
        if (entryList.getFirst().isEmpty()&&entryList.getLast().isEmpty()){
            return null;
        }

        int entrySize = Math.max(entryList.getFirst().size(),entryList.getLast().size());

        double MAX_WIDTH = (WIDTH / entrySize) * (entrySize - 1);
        final double WIDTH_PER_ENTRY = (MAX_WIDTH / ((entrySize))) /3;

        // Bestimmung des höchsten Wertes in der Liste der Balkendiagrammeinträge
        BarChartEntry highestEntryEinspeisen = Collections.max(entryList.getFirst(), BarChartEntry::compareTo);
        BarChartEntry highestEntryVerbrauch = Collections.max(entryList.getFirst(), BarChartEntry::compareTo);
        double highestValue = Math.max(highestEntryEinspeisen.value, highestEntryVerbrauch.value);
        if (highestValue == 0) {
            return null;
        }

        double heightIndex = (HEIGHT / highestValue);

        // Erzeugung eines Canvas-Objekts, das etwas größer ist als die angegebene Breite und Höhe (wegen PADDING)
        Canvas c = new Canvas(WIDTH + PADDING + 40, HEIGHT + PADDING + 40);
        GraphicsContext gc = c.getGraphicsContext2D();

        double width = WIDTH_PER_ENTRY + PADDING;
        double initialDrawHeight_X = c.getHeight() - PADDING;
        gc.setFont(Font.font(8));

        int y = 0;
        int index = 0;
        List<BarChartEntry> verbrauchsliste = entryList.getLast();
        List<BarChartEntry> einspeisenliste = entryList.getFirst();
        List<BarChartEntry> benutzeListe;

        if (einspeisenliste.isEmpty()){
            benutzeListe = verbrauchsliste;
            verbrauchsliste = einspeisenliste;
        }else {
            benutzeListe = einspeisenliste;
        }

        for (BarChartEntry e : benutzeListe) {
            //Für Einspeisen
            // Setzt die Füllfarbe für den aktuellen Balken
            gc.setFill(Color.rgb(100, 160, (y * (255 / entryList.getFirst().size()))));
            double barHeight = e.value * heightIndex;

            // Zeichnet den Balken
            gc.fillRect(width, initialDrawHeight_X - e.value * heightIndex, WIDTH_PER_ENTRY, barHeight);

            // Setzt die Füllfarbe auf Schwarz für den Text
            gc.setFill(Color.BLACK);
            gc.fillText(e.name, width, initialDrawHeight_X + 15);
            //für Verbrauch
            gc.setFill(Color.BLACK);
            width += WIDTH_PER_ENTRY;
            try {
                    double value = verbrauchsliste.get(index).value;
                    barHeight = value * heightIndex;
                    // Zeichnet den Balken
                    gc.fillRect(width, initialDrawHeight_X - value * heightIndex, WIDTH_PER_ENTRY, barHeight);
            }finally {
                // Aktualisiert die Breite für den nächsten Balken
                width += WIDTH_PER_ENTRY * 2;
                y++;
                index++;
            }
            if (interval == Interval.DAILY) {
                if ((y + 1) % 8 == 0) {
                    gc.strokeLine(width + WIDTH_PER_ENTRY / 2, initialDrawHeight_X, width + WIDTH_PER_ENTRY / 2, initialDrawHeight_X + 5);
                }
            }
            if (interval == Interval.WEEKLY) {
                if (!e.name.isEmpty()) {
                    gc.strokeLine(width + WIDTH_PER_ENTRY / 2, initialDrawHeight_X, width + WIDTH_PER_ENTRY / 2, initialDrawHeight_X + 5);
                }
            }
        }

        // Zeichnet den Nullpunkt
            gc.fillText("0", PADDING - 10, initialDrawHeight_X + 5);

        // Anzahl der Unterteilungen auf der Y-Achse
        final int UNTERTEILUNGENY = 5;
        for (int i = 1; i < UNTERTEILUNGENY + 1; i++) {
            gc.strokeLine(PADDING - 2, initialDrawHeight_X - ((HEIGHT / UNTERTEILUNGENY) * i), PADDING + 2, initialDrawHeight_X - ((HEIGHT / UNTERTEILUNGENY) * i));
            gc.fillText(String.format("%.1f", ((HEIGHT / heightIndex) / UNTERTEILUNGENY) * i), PADDING - 30, initialDrawHeight_X - ((HEIGHT / UNTERTEILUNGENY) * i) + 3);
        }

        // Zeichnet die Y-Achse
        gc.strokeLine(PADDING, initialDrawHeight_X, PADDING, initialDrawHeight_X - (highestValue * heightIndex));

        // Zeichnet die X-Achse
        gc.strokeLine(PADDING, initialDrawHeight_X, PADDING + MAX_WIDTH + WIDTH_PER_ENTRY, initialDrawHeight_X);

        return c;
    }





}
