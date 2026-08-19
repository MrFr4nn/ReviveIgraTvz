package hr.tvz.revive.kontroler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class KontrolerKatalog {

    @FXML
    private Label labelaKatalog;

    public void postaviTekst(String tekst) {
        labelaKatalog.setText(tekst);
    }

    @FXML
    private void zatvoriProzor() {
        Stage prozor = (Stage) labelaKatalog.getScene().getWindow();
        prozor.close();
    }
}