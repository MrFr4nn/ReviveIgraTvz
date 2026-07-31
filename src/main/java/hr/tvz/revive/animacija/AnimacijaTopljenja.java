package hr.tvz.revive.animacija;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimacijaTopljenja {

    private static final Duration TRAJANJE_ANIMACIJE = Duration.millis(800);

    public void pokreniAnimacijuNaPolju(Rectangle prikazPolja) {
        FillTransition tranzicijaBoje = new FillTransition(TRAJANJE_ANIMACIJE, prikazPolja);
        tranzicijaBoje.setFromValue(Color.DODGERBLUE);
        tranzicijaBoje.setToValue(Color.WHITESMOKE);
        tranzicijaBoje.play();
    }

    public void resetirajBojuPolja(Rectangle prikazPolja) {
        prikazPolja.setFill(Color.DODGERBLUE);
    }
}