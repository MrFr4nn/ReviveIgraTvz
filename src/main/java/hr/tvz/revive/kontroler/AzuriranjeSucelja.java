package hr.tvz.revive.kontroler;

import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PermafrostPloca;
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
    private static final String POLEDJINA_KARTE = "Okrenuta karta";

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

    public void azurirajCijeloSucelje(ReviveEngine reviveEngine, Label labelaTrenutniIgrac, Label labelaRunda,
                                      ListView<String> listaKarataIgrac1, ListView<String> listaKarataIgrac2,
                                      Label labelaStanjeIgrac1, Label labelaStanjeIgrac2) {

        List<Igrac> igraci = reviveEngine.getIgraci();
        Igrac prviIgrac = igraci.get(0);
        Igrac drugiIgrac = igraci.get(1);
        boolean prviIgracNaPotezu = reviveEngine.getIndeksIgracaNaPotezu() == 0;

        labelaTrenutniIgrac.setText("Na potezu: " + reviveEngine.getIgracNaPotezu().getImeIgraca());
        labelaRunda.setText("Runda: " + reviveEngine.getTrenutnaRunda() + " / " + ReviveEngine.BROJ_RUNDI);

        azurirajListuKarata(prviIgrac, listaKarataIgrac1, prviIgracNaPotezu);
        azurirajListuKarata(drugiIgrac, listaKarataIgrac2, !prviIgracNaPotezu);

        labelaStanjeIgrac1.setText(formatirajStanjeIgraca(prviIgrac));
        labelaStanjeIgrac2.setText(formatirajStanjeIgraca(drugiIgrac));
    }

    private void azurirajListuKarata(Igrac igrac, ListView<String> listaKarata, boolean prikaziPuniNaziv) {
        List<String> nazivKarataZaPrikaz = new ArrayList<>();
        for (Karta karta : igrac.getRukaKarata()) {
            nazivKarataZaPrikaz.add(prikaziPuniNaziv ? karta.toString() : POLEDJINA_KARTE);
        }
        listaKarata.getItems().setAll(nazivKarataZaPrikaz);
    }

    private String formatirajStanjeIgraca(Igrac igrac) {
        return "Hrana: " + igrac.getHrana()
                + "   Zupcanici: " + igrac.getZupcanici()
                + "   Kristali: " + igrac.getKristali()
                + "\nMasine: " + igrac.getIzgradjeneMasine().size()
                + "   Zapisi: " + igrac.getBrojNaucenihZapisa()
                + "   Eksperimenti: " + igrac.getBrojIzucenihEksperimenata()
                + "\nBodovi: " + igrac.izracunajUkupneBodoveNaKraju();
    }

    public String azurirajGumbeRadnika(Karta odabranaKarta, Button gumbExplorer, Button gumbBuilder,
                                       Button gumbScholar, Button gumbScientist) {
        TipRadnika potrebniTip = odabranaKarta == null ? null : odabranaKarta.getTipAkcije().getPovezaniTipRadnika();

        gumbExplorer.setDisable(potrebniTip != TipRadnika.EXPLORER);
        gumbBuilder.setDisable(potrebniTip != TipRadnika.BUILDER);
        gumbScholar.setDisable(potrebniTip != TipRadnika.SCHOLAR);
        gumbScientist.setDisable(potrebniTip != TipRadnika.SCIENTIST);

        if (potrebniTip == null) {
            return "Odaberi kartu iz svoje ruke.";
        } else if (potrebniTip == TipRadnika.EXPLORER) {
            return "Karta zahtijeva EXPLORER - klikni Permafrost polje.";
        }
        return "Karta zahtijeva " + potrebniTip + " - klikni taj gumb.";
    }
}