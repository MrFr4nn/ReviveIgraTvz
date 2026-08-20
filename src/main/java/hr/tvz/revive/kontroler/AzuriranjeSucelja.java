package hr.tvz.revive.kontroler;

import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.PoljePermafrosta;
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
    private static final String POLEDJINA_KARTE = "🂠 Skrivena karta";

    public Rectangle[][] izgradiPermafrostMrezu(GridPane mrezaPermafrosta) {
        int velicina = PermafrostPloca.VELICINA;
        Rectangle[][] pravokutnici = new Rectangle[velicina][velicina];

        for (int redak = 0; redak < velicina; redak++) {
            for (int stupac = 0; stupac < velicina; stupac++) {
                Rectangle pravokutnikPolja = new Rectangle(VELICINA_PRAVOKUTNIKA_POLJA, VELICINA_PRAVOKUTNIKA_POLJA);
                pravokutnikPolja.setFill(Color.LIGHTGRAY);
                pravokutnikPolja.setArcWidth(12);
                pravokutnikPolja.setArcHeight(12);
                pravokutnici[redak][stupac] = pravokutnikPolja;
                mrezaPermafrosta.add(pravokutnikPolja, stupac, redak);
            }
        }
        return pravokutnici;
    }

    public void postaviKlikoveNaPolja(Rectangle[][] pravokutnici, KlikNaPolje klikNaPolje) {
        for (int redak = 0; redak < pravokutnici.length; redak++) {
            for (int stupac = 0; stupac < pravokutnici[redak].length; stupac++) {
                int r = redak;
                int s = stupac;
                pravokutnici[redak][stupac].setOnMouseClicked(dogadjaj -> klikNaPolje.obradiKlik(r, s));
            }
        }
    }

    public interface KlikNaPolje {
        void obradiKlik(int redak, int stupac);
    }

    public void azurirajBojePolja(PermafrostPloca ploca, Rectangle[][] pravokutnici) {
        for (PoljePermafrosta polje : ploca.getPolja()) {
            Rectangle pravokutnik = pravokutnici[polje.getRedak()][polje.getStupac()];
            if (!polje.isZauzeto()) {
                pravokutnik.setFill(Color.LIGHTGRAY);
            } else if (polje.getIndeksVlasnika() == 0) {
                pravokutnik.setFill(Color.SEAGREEN);
            } else {
                pravokutnik.setFill(Color.DODGERBLUE);
            }
        }
    }

    public void azurirajCijeloSucelje(ReviveEngine reviveEngine, Label labelaTrenutniIgrac, Label labelaRunda,
                                      ListView<String> listaKarataIgrac1, ListView<String> listaKarataIgrac2,
                                      Label labelaStanjeIgrac1, Label labelaStanjeIgrac2) {

        List<Igrac> igraci = reviveEngine.getIgraci();
        Igrac prviIgrac = igraci.get(0);
        Igrac drugiIgrac = igraci.get(1);
        boolean prviIgracNaPotezu = reviveEngine.getIndeksIgracaNaPotezu() == 0;

        labelaTrenutniIgrac.setText("Na potezu: " + reviveEngine.getIgracNaPotezu().getImeIgraca());
        labelaTrenutniIgrac.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-padding: 4 14; -fx-background-radius: 8; -fx-background-color: "
                + (prviIgracNaPotezu ? "#2ecc71" : "#3498db") + ";");
        int trenutnaRunda = reviveEngine.getTrenutnaRunda();
        boolean zavrsnaRunda = trenutnaRunda == ReviveEngine.BROJ_RUNDI;
        labelaRunda.setText("Runda " + trenutnaRunda + "/" + ReviveEngine.BROJ_RUNDI
                + (zavrsnaRunda ? " - Zavrsna runda" : ""));

        azurirajListuKarata(prviIgrac, listaKarataIgrac1, prviIgracNaPotezu);
        azurirajListuKarata(drugiIgrac, listaKarataIgrac2, !prviIgracNaPotezu);

        labelaStanjeIgrac1.setText(formatirajStanjeIgraca(prviIgrac));
        labelaStanjeIgrac2.setText(formatirajStanjeIgraca(drugiIgrac));
    }

    public void azurirajGumbeRadnika(Igrac igracNaPotezu, Button gumbExplorer, Button gumbBuilder,
                                     Button gumbScholar, Button gumbScientist) {
        azurirajJedanGumb(gumbExplorer, "EXPLORER", "1 kristal", igracNaPotezu, TipRadnika.EXPLORER);
        azurirajJedanGumb(gumbBuilder, "BUILDER", "2 zupcanika", igracNaPotezu, TipRadnika.BUILDER);
        azurirajJedanGumb(gumbScholar, "SCHOLAR", "2 hrane", igracNaPotezu, TipRadnika.SCHOLAR);
        azurirajJedanGumb(gumbScientist, "SCIENTIST", "1 kristal", igracNaPotezu, TipRadnika.SCIENTIST);
    }

    private void azurirajJedanGumb(Button gumb, String naziv, String cijena, Igrac igrac, TipRadnika tip) {
        Radnik radnik = igrac.pronadjiNepostavljenogRadnika(tip);
        boolean dostupan = radnik != null;
        gumb.setText(naziv + " (" + cijena + ") " + (dostupan ? "1/1" : "0/1"));
        gumb.setDisable(!dostupan);
    }

    private void azurirajListuKarata(Igrac igrac, ListView<String> listaKarata, boolean prikaziPuniNaziv) {
        List<String> nazivi = new ArrayList<>();
        for (Karta karta : igrac.getRukaKarata()) {
            nazivi.add(prikaziPuniNaziv ? karta.toString() : POLEDJINA_KARTE);
        }
        listaKarata.getItems().setAll(nazivi);
    }

    private String formatirajStanjeIgraca(Igrac igrac) {
        return "Hrana: " + igrac.getHrana()
                + "   Zupcanici: " + igrac.getZupcanici()
                + "   Kristali: " + igrac.getKristali()
                + "\nMasine: " + igrac.getIzgradjeneMasine().size()
                + "   Bodovi: " + igrac.izracunajUkupneBodoveNaKraju();
    }
}