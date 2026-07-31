package hr.tvz.revive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GlavnaAplikacija extends Application {

    private static final String NASLOV_PROZORA = "Revive";
    private static final String PUTANJA_GLAVNOG_FXML = "/hr/tvz/revive/glavni-ekran.fxml";
    private static final int SIRINA_PROZORA = 1000;
    private static final int VISINA_PROZORA = 700;

    @Override
    public void start(Stage glavnaPozornica) throws IOException {
        FXMLLoader ucitavacFxml = new FXMLLoader(getClass().getResource(PUTANJA_GLAVNOG_FXML));
        Parent korijenskiElement = ucitavacFxml.load();

        Scene glavnaScena = new Scene(korijenskiElement, SIRINA_PROZORA, VISINA_PROZORA);

        glavnaPozornica.setTitle(NASLOV_PROZORA);
        glavnaPozornica.setScene(glavnaScena);
        glavnaPozornica.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}