package hr.tvz.revive.kontroler;

import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PermafrostPloca;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class AzuriranjeSucelja {

    private static final int VELICINA_PRAVOKUTNIKA_POLJA = 60;

    public Rectangle[][] izgradiPermafrostMrezu(GridPane mrezaPermafrosta) {
        int velicina = PermafrostPloca.VELICINA;
        Rectangle[][] pravokutniciPermafrosta = new Rectangle[velicina][velicina];

        for (int redak = 0; redak < velicina; redak++) {
            for (int stupac = 0; stupac < velicina; stupac++) {
                Rectangle pravokutnikPolja =
                        new Rectangle(VELICINA_PRAVOKUTNIKA_POLJA, VELICINA_PRAVOKUTNIKA_POLJA);
                pravokutnikPolja.setFill(Color.DODGERBLUE);
                pravokutniciPermafrosta[redak][stupac] = pravokutnikPolja;
                mrezaPermafrosta.add(pravokutnikPolja, stupac, redak);
            }
        }

        return pravokutniciPermafrosta;
    }

    public void azurirajCijeloSucelje(ReviveEngine reviveEngine, Label labelaTrenutniIgrac,
                                      Label labelaRunda, ListView<String> listaRukaKarata,
                                      Label labelaResursiIgrac1, Label labelaResursiIgrac2) {

        Igrac igracNaPotezu = reviveEngine.getIgracNaPotezu();

        labelaTrenutniIgrac.setText("Na potezu: " + igracNaPotezu.getImeIgraca());
        labelaRunda.setText("Runda: " + reviveEngine.getTrenutnaRunda() + " / " + ReviveEngine.BROJ_RUNDI);

        azurirajListuRukeKarata(igracNaPotezu, listaRukaKarata);
        azurirajLabeleResursa(reviveEngine, labelaResursiIgrac1, labelaResursiIgrac2);
    }

    private void azurirajListuRukeKarata(Igrac igracNaPotezu, ListView<String> listaRukaKarata) {
        List<String> nazivKarataZaPrikaz = new ArrayList<>();
        for (Karta karta : igracNaPotezu.getRukaKarata()) {
            nazivKarataZaPrikaz.add(karta.toString());
        }
        listaRukaKarata.getItems().setAll(nazivKarataZaPrikaz);
    }

    private void azurirajLabeleResursa(ReviveEngine reviveEngine, Label labelaResursiIgrac1, Label labelaResursiIgrac2) {
        List<Igrac> igraci = reviveEngine.getIgraci();
        Igrac prviIgrac = igraci.get(0);
        Igrac drugiIgrac = igraci.get(1);

        labelaResursiIgrac1.setText(formatirajStanjeIgraca(prviIgrac));
        labelaResursiIgrac2.setText(formatirajStanjeIgraca(drugiIgrac));
    }

    private String formatirajStanjeIgraca(Igrac igrac) {
        return igrac.getImeIgraca() + ": Hrana=" + igrac.getHrana()
                + " Zupcanici=" + igrac.getZupcanici()
                + " Bodovi=" + igrac.izracunajUkupneBodoveNaKraju();
    }
}