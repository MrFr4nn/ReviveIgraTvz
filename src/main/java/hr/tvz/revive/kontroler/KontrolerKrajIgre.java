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
    private Runnable akcijaUcitajIgru;
    private Runnable akcijaPrikaziKatalog;

    public void postaviPodatke(String tekstPobjednika, String tekstRezultata,
                               Runnable akcijaIgrajPonovno, Runnable akcijaPrikaziReplay,
                               Runnable akcijaUcitajIgru, Runnable akcijaPrikaziKatalog) {
        labelaPobjednik.setText(tekstPobjednika);
        labelaRezultati.setText(tekstRezultata);
        this.akcijaIgrajPonovno = akcijaIgrajPonovno;
        this.akcijaPrikaziReplay = akcijaPrikaziReplay;
        this.akcijaUcitajIgru = akcijaUcitajIgru;
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
    }

    @FXML
    private void ucitajIgru() {
        akcijaUcitajIgru.run();
        zatvoriProzor();
    }

    @FXML
    private void prikaziKatalogRadnika() {
        akcijaPrikaziKatalog.run();
    }

    @FXML
    private void zatvoriProzor() {
        Stage prozor = (Stage) labelaPobjednik.getScene().getWindow();
        prozor.close();
    }
}