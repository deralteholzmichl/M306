package com.ubs.helper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.util.ArrayList;
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
        int UNTERTEILUNGENX = DatenBeschrX.size()-1;
        int UNTERTEILUNGENY = 10;

        // Passe die Schriftgröße an, wenn zu viele Unterteilungen vorhanden sind
        if (UNTERTEILUNGENX > 10 || UNTERTEILUNGENY > 10) {
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
            // Nullpunkt
           // gc.fillText(DatenBeschrX.get(0), PADDING, canvas.getHeight() - PADDING / UNTERTEILUNGENX);
            int lasi= 0;
            for (int i = 0; i <= UNTERTEILUNGENX; i++) {
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
}
