package hr.tvz.revive.kontroler;

import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.Radnik;
import hr.tvz.revive.model.TipRadnika;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class AzuriranjeSucelja {

    private static final int VELICINA_PRAVOKUTNIKA_POLJA = 70;

    public Rectangle[][] izgradiPermafrostMrezu(GridPane mrezaPermafrosta) {
        int velicina = PermafrostPloca.VELICINA;
        Rectangle[][] pravokutniciPermafrosta = new Rectangle[velicina][velicina];

        for (int redak = 0; redak < velicina; redak++) {
            for (int stupac = 0; stupac < velicina; stupac++) {
                Rectangle pravokutnikPolja =
                        new Rectangle(VELICINA_PRAVOKUTNIKA_POLJA, VELICINA_PRAVOKUTNIKA_POLJA);
                pravokutnikPolja.setFill(Color.DODGERBLUE);
                pravokutnikPolja.setArcWidth(12);
                pravokutnikPolja.setArcHeight(12);
                pravokutniciPermafrosta[redak][stupac] = pravokutnikPolja;
                mrezaPermafrosta.add(pravokutnikPolja, stupac, redak);
            }
        }

        return pravokutniciPermafrosta;
    }

    public void postaviKlikoveNaPolja(Rectangle[][] pravokutnici, KlikNaPolje klikNaPolje) {
        for (int redak = 0; redak < pravokutnici.length; redak++) {
            for (int stupac = 0; stupac < pravokutnici[redak].length; stupac++) {
                int redakZaKlik = redak;
                int stupacZaKlik = stupac;
                pravokutnici[redak][stupac].setOnMouseClicked(
                        dogadjaj -> klikNaPolje.obradiKlik(redakZaKlik, stupacZaKlik));
            }
        }
    }

    public interface KlikNaPolje {
        void obradiKlik(int redak, int stupac);
    }

    public void azurirajCijeloSucelje(ReviveEngine reviveEngine, Label labelaTrenutniIgrac,
                                      Label labelaRunda, ListView<String> listaRukaKarata,
                                      Label labelaResursiIgrac1, Label labelaResursiIgrac2,
                                      Label labelaStatusRadnika) {

        Igrac igracNaPotezu = reviveEngine.getIgracNaPotezu();

        labelaTrenutniIgrac.setText("Na potezu: " + igracNaPotezu.getImeIgraca());
        labelaRunda.setText("Runda: " + reviveEngine.getTrenutnaRunda() + " / " + ReviveEngine.BROJ_RUNDI);

        azurirajListuRukeKarata(igracNaPotezu, listaRukaKarata);
        azurirajLabeleResursa(reviveEngine, labelaResursiIgrac1, labelaResursiIgrac2);
        labelaStatusRadnika.setText(formatirajStatusRadnika(igracNaPotezu));
    }

    private String formatirajStatusRadnika(Igrac igracNaPotezu) {
        StringBuilder statusRadnika = new StringBuilder();
        for (Radnik radnik : igracNaPotezu.getRadnici()) {
            String oznakaStatusa = radnik.isPostavljen() ? "ISKORISTEN" : "dostupan";
            statusRadnika.append(radnik.getTip()).append(": ").append(oznakaStatusa).append("\n");
        }
        return statusRadnika.toString().trim();
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
        return igrac.getImeIgraca() + "\n  Hrana: " + igrac.getHrana()
                + "\n  Zupcanici: " + igrac.getZupcanici()
                + "\n  Bodovi: " + igrac.izracunajUkupneBodoveNaKraju();
    }

    public String azurirajGumbeRadnika(Karta odabranaKarta, Button gumbExplorer, Button gumbBuilder,
                                       Button gumbScholar, Button gumbScientist) {
        TipRadnika potrebniTip = odabranaKarta == null ? null : odabranaKarta.getTipAkcije().getPovezaniTipRadnika();

        gumbExplorer.setDisable(potrebniTip != TipRadnika.EXPLORER);
        gumbBuilder.setDisable(potrebniTip != TipRadnika.BUILDER);
        gumbScholar.setDisable(potrebniTip != TipRadnika.SCHOLAR);
        gumbScientist.setDisable(potrebniTip != TipRadnika.SCIENTIST);

        if (potrebniTip == null) {
            return "Odaberi kartu iz ruke da vidis koji radnik ide uz nju.";
        } else if (potrebniTip == TipRadnika.EXPLORER) {
            return "Karta zahtijeva: " + potrebniTip + " - klikni Permafrost polje.";
        }
        return "Karta zahtijeva: " + potrebniTip + " - klikni 'Odigraj potez'.";
    }
}