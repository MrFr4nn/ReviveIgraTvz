package hr.tvz.revive.animacija;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class PlutajucaPoruka {

    private static final Duration TRAJANJE_PRIKAZA = Duration.millis(1400);
    private static final Duration TRAJANJE_NESTAJANJA = Duration.millis(600);

    public void prikaziPoruku(Pane sloj, String tekst, double x, double y) {
        Label labelaPoruke = new Label(tekst);
        labelaPoruke.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #27ae60; "
                + "-fx-background-color: white; -fx-padding: 4 8; -fx-background-radius: 6;");
        labelaPoruke.setLayoutX(x);
        labelaPoruke.setLayoutY(y);
        sloj.getChildren().add(labelaPoruke);

        PauseTransition pauza = new PauseTransition(TRAJANJE_PRIKAZA);
        FadeTransition nestajanje = new FadeTransition(TRAJANJE_NESTAJANJA, labelaPoruke);
        nestajanje.setFromValue(1.0);
        nestajanje.setToValue(0.0);
        nestajanje.setOnFinished(dogadjaj -> sloj.getChildren().remove(labelaPoruke));

        new SequentialTransition(pauza, nestajanje).play();
    }
}