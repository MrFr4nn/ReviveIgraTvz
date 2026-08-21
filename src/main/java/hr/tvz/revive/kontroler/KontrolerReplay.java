package hr.tvz.revive.kontroler;

import hr.tvz.revive.xml.PodatakOPotezu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.List;

public class KontrolerReplay {

    @FXML
    private Label labelaBrojKoraka;

    @FXML
    private Label labelaTekstPoteza;

    private List<PodatakOPotezu> svilPotezi;
    private int trenutniIndeksKoraka;

    public void postaviPoteze(List<PodatakOPotezu> svilPotezi) {
        this.svilPotezi = svilPotezi;
        this.trenutniIndeksKoraka = svilPotezi.isEmpty() ? -1 : 0;
        azurirajPrikaz();
    }

    @FXML
    private void prikaziSljedeciKorak() {
        if (svilPotezi == null || svilPotezi.isEmpty()) {
            return;
        }
        if (trenutniIndeksKoraka < svilPotezi.size() - 1) {
            trenutniIndeksKoraka++;
        }
        azurirajPrikaz();
    }

    @FXML
    private void prikaziPrethodniKorak() {
        if (svilPotezi == null || svilPotezi.isEmpty()) {
            return;
        }
        if (trenutniIndeksKoraka > 0) {
            trenutniIndeksKoraka--;
        }
        azurirajPrikaz();
    }

    private void azurirajPrikaz() {
        if (svilPotezi == null || svilPotezi.isEmpty()) {
            labelaBrojKoraka.setText("Korak 0 / 0");
            labelaTekstPoteza.setText("Nema još zapisanih poteza.");
            return;
        }

        labelaBrojKoraka.setText("Korak " + (trenutniIndeksKoraka + 1) + " / " + svilPotezi.size());
        labelaTekstPoteza.setText(svilPotezi.get(trenutniIndeksKoraka).toString());
    }
}