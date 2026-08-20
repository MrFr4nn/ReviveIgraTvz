package hr.tvz.revive.kontroler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class KontrolerKrajIgre {

    @FXML
    private Label labelaPobjednik;

    @FXML
    private Label labelaRezultati;

    private Runnable akcijaIgrajPonovno;
    private Runnable akcijaPrikaziReplay;
    private Runnable akcijaPrikaziKatalog;

    public void postaviPodatke(String tekstPobjednika, String tekstRezultata,
                               Runnable akcijaIgrajPonovno, Runnable akcijaPrikaziReplay,
                               Runnable akcijaPrikaziKatalog) {
        labelaPobjednik.setText(tekstPobjednika);
        labelaRezultati.setText(tekstRezultata);
        this.akcijaIgrajPonovno = akcijaIgrajPonovno;
        this.akcijaPrikaziReplay = akcijaPrikaziReplay;
        this.akcijaPrikaziKatalog = akcijaPrikaziKatalog;
    }

    @FXML
    private void igrajPonovno() {
        akcijaIgrajPonovno.run();
        zatvoriProzor();
    }

    @FXML
    private void prikaziReplay() {
        akcijaPrikaziReplay.run();
        zatvoriProzor();
    }

    @FXML
    private void prikaziKatalogRadnika() {
        akcijaPrikaziKatalog.run();
        zatvoriProzor();
    }

    @FXML
    private void zatvoriProzor() {
        Stage prozor = (Stage) labelaPobjednik.getScene().getWindow();
        prozor.close();
    }
}