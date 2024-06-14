package com.ubs.helper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    public static Canvas drawLineDiagram(List<Double> list, double MAX_HEIGHT, double MAX_WIDTH, double PADDING, boolean BESCHRIFTUNGX, boolean BESCHRIFTUNGY, List<String> DatenBeschrX) {

        // Berechne die erforderliche Gesamthöhe, beginnend bei 0
        double requiredHeight = 0;
        for (double i : list) {
            requiredHeight += i;
        }

        // Berechne den Höhenindex, um die Höhen der Linien zu skalieren
        double heightIndex = (MAX_HEIGHT / requiredHeight);
        ArrayList<Double> indexedListHeight = new ArrayList<>();
        for (double i : list) {
            // Skaliere jede Höhe in der Liste entsprechend dem Höhenindex
            indexedListHeight.add(i * heightIndex);
        }

        // Berechne die Breite pro Intervall basierend auf der Anzahl der Elemente in der Liste
        double WIDTH_PER_INTERVAL = MAX_WIDTH / (list.size()-1);
        // Erstelle eine neue Canvas mit den angegebenen Breiten und Höhen plus Padding
        Canvas canvas = new Canvas(MAX_WIDTH + PADDING + 40, MAX_HEIGHT + PADDING + 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Setze die Startposition der Höhe unter Berücksichtigung des Paddings und der ersten Höhe in der skalierten Liste
        double height = canvas.getHeight() - PADDING - indexedListHeight.get(0);
        double width = 0;
        gc.fillOval(width - 3+PADDING, height - 3, 5, 5);

        // Zeichne die Linien und Punkte basierend auf den skalierten Höhen und Breiten
        for (int i = 1; i < indexedListHeight.size(); i++) {
            // Zeichne eine Linie vom aktuellen Punkt zum nächsten
            gc.strokeLine(width + PADDING, height, width + WIDTH_PER_INTERVAL + PADDING, (height - indexedListHeight.get(i)));
            // Aktualisiere die Höhe für den nächsten Punkt
            height -= indexedListHeight.get(i);
            // Aktualisiere die Breite für den nächsten Punkt
            width += WIDTH_PER_INTERVAL;
            // Zeichne einen Punkt am aktuellen Punkt
            gc.fillOval(width - 3+PADDING, height - 3, 5, 5);
        }

        // Anzahl der Unterteilungen für die X- und Y-Achsenbeschriftungen
        int UNTERTEILUNGENY = 10;

        // Passe die Schriftgröße an, wenn zu viele Unterteilungen vorhanden sind
        if (UNTERTEILUNGENY > 10) {
            gc.setFont(Font.font(10));
        }

        // Zeichne Beschriftungen auf der Y-Achse
        if (BESCHRIFTUNGY) {
            // Nullpunkt
            gc.fillText("0", (double) PADDING / 5, canvas.getHeight() - PADDING);
            for (int i = 1; i < UNTERTEILUNGENY + 1; i++) {
                // Zeichne Markierungen und Beschriftungen auf der Y-Achse
                gc.strokeLine(PADDING - 2, canvas.getHeight() - PADDING - ((requiredHeight * heightIndex / UNTERTEILUNGENY) * i), PADDING + 2, canvas.getHeight() - PADDING - ((requiredHeight * heightIndex / UNTERTEILUNGENY) * i));
                gc.fillText(String.format("%.1f", ((requiredHeight / UNTERTEILUNGENY) * i)), PADDING / 5, canvas.getHeight() - PADDING - ((requiredHeight * heightIndex / UNTERTEILUNGENY) * i) + 5);
            }
        }

        // Zeichne Beschriftungen auf der X-Achse
        if (BESCHRIFTUNGX) {
            int UNTERTEILUNGENX = DatenBeschrX.size()-1;
            if (UNTERTEILUNGENX > 10) {
                gc.setFont(Font.font(10));
            }
            // Nullpunkt
           // gc.fillText(DatenBeschrX.get(0), PADDING, canvas.getHeight() - PADDING / UNTERTEILUNGENX);
            for (int i = 0; i <= UNTERTEILUNGENX; i++) {
                gc.setFont(Font.font(8));
                // Zeichne Markierungen und Beschriftungen auf der X-Achse
                if (i == 0){
                    gc.fillText(DatenBeschrX.get(i), PADDING, canvas.getHeight() - PADDING / 5);
                }else {
                    gc.strokeLine(((MAX_WIDTH / UNTERTEILUNGENX) * i)+PADDING, (canvas.getHeight() - PADDING - 2), ((MAX_WIDTH/ UNTERTEILUNGENX) * i)+PADDING, (canvas.getHeight() - PADDING + 2));
                    gc.fillText(DatenBeschrX.get(i), ((MAX_WIDTH / UNTERTEILUNGENX) * i)+PADDING -10, canvas.getHeight() - PADDING / 5);
                }
            }
        }

        // Zeichne die X-Achse
        gc.strokeLine(PADDING, canvas.getHeight() - PADDING, MAX_WIDTH+PADDING, canvas.getHeight() - PADDING);
        // Zeichne die Y-Achse
        gc.strokeLine(PADDING, canvas.getHeight() - PADDING, PADDING, height);

        return canvas;
    }
    public static Canvas drawBarChart(double WIDTH, double HEIGHT, double PADDING, List<BarChartEntry> entryList) {
        // Berechnung der maximalen Breite für die Balken
        double MAX_WIDTH = (WIDTH / entryList.size()) * entryList.size() - 1;
        final double WIDTH_PER_ENTRY = MAX_WIDTH / (entryList.size() * 2);

        // Bestimmung des höchsten Wertes in der Liste der Balkendiagrammeinträge
        BarChartEntry highestEntry = Collections.max(entryList, BarChartEntry::compareTo);
        double heightIndex = (HEIGHT / highestEntry.value);

        // Erzeugung eines Canvas-Objekts, das etwas größer ist als die angegebene Breite und Höhe (wegen PADDING)
        Canvas c = new Canvas(WIDTH + PADDING + 40, HEIGHT + PADDING + 40);
        GraphicsContext gc = c.getGraphicsContext2D();

        double width = WIDTH_PER_ENTRY + PADDING;
        double initialDrawHeight_X = c.getHeight() - PADDING;
        gc.setFont(Font.font(8));

        int y = 0;
        for (BarChartEntry e : entryList) {
            // Setzt die Füllfarbe für den aktuellen Balken
            gc.setFill(Color.rgb(100, 160, (y * (255 / entryList.size()))));
            double barHeight = e.value * heightIndex;

            // Zeichnet den Balken
            gc.fillRect(width, initialDrawHeight_X - e.value * heightIndex, WIDTH_PER_ENTRY, barHeight);

            // Setzt die Füllfarbe auf Schwarz für den Text
            gc.setFill(Color.BLACK);
            gc.fillText(e.name, width, initialDrawHeight_X + 15);

            // Aktualisiert die Breite für den nächsten Balken
            width += WIDTH_PER_ENTRY * 2;
            y++;
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
        gc.strokeLine(PADDING, initialDrawHeight_X, PADDING, initialDrawHeight_X - (highestEntry.value * heightIndex));

        // Zeichnet die X-Achse
        gc.strokeLine(PADDING, initialDrawHeight_X, PADDING + MAX_WIDTH + WIDTH_PER_ENTRY, initialDrawHeight_X);

        return c;
    }





}
