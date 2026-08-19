package hr.tvz.revive.animacija;

import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimacijaTopljenja {

    private static final Duration TRAJANJE_BOJE = Duration.millis(500);
    private static final Duration TRAJANJE_ISKOKA = Duration.millis(250);
    private static final double FAKTOR_ISKOKA = 1.15;

    public void pokreniAnimacijuNaPolju(Rectangle prikazPolja, Color bojaVlasnika) {
        FillTransition tranzicijaBoje = new FillTransition(TRAJANJE_BOJE, prikazPolja);
        tranzicijaBoje.setToValue(bojaVlasnika);

        ScaleTransition iskok = new ScaleTransition(TRAJANJE_ISKOKA, prikazPolja);
        iskok.setToX(FAKTOR_ISKOKA);
        iskok.setToY(FAKTOR_ISKOKA);
        iskok.setAutoReverse(true);
        iskok.setCycleCount(2);

        ParallelTransition zajednickaAnimacija = new ParallelTransition(tranzicijaBoje, iskok);
        zajednickaAnimacija.play();
    }
}