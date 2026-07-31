package hr.tvz.revive.kontroler;

import hr.tvz.revive.animacija.AnimacijaTopljenja;
import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.Bodovanje;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.engine.RezultatPoteza;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.TipRadnika;
import hr.tvz.revive.xml.ZapisPoteza;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GlavniKontroler {

    @FXML
    private Label labelaTrenutniIgrac;

    @FXML
    private Label labelaRunda;

    @FXML
    private Label labelaPorukaPoteza;

    @FXML
    private Label labelaResursiIgrac1;

    @FXML
    private Label labelaResursiIgrac2;

    @FXML
    private Label labelaKrajIgre;

    @FXML
    private ListView<String> listaRukaKarata;

    @FXML
    private GridPane mrezaPermafrosta;

    private ReviveEngine reviveEngine;
    private ZapisPoteza zapisPoteza;
    private AzuriranjeSucelja azuriranjeSucelja;
    private PomocneAkcijeKontrolera pomocneAkcijeKontrolera;
    private TipRadnika odabraniTipRadnika;
    private Rectangle[][] pravokutniciPermafrosta;

    @FXML
    public void initialize() {
        reviveEngine = new ReviveEngine();
        zapisPoteza = new ZapisPoteza();
        azuriranjeSucelja = new AzuriranjeSucelja();
        pomocneAkcijeKontrolera = new PomocneAkcijeKontrolera();
        odabraniTipRadnika = null;

        reviveEngine.pokreniNovuIgru("Igrac 1", "Igrac 2");
        zapisPoteza.zapocniZapis();

        pravokutniciPermafrosta = azuriranjeSucelja.izgradiPermafrostMrezu(mrezaPermafrosta);
        azurirajSucelje();
    }

    @FXML
    private void odaberiExplorer() {
        postaviOdabraniTipRadnika(TipRadnika.EXPLORER);
    }

    @FXML
    private void odaberiBuilder() {
        postaviOdabraniTipRadnika(TipRadnika.BUILDER);
    }

    @FXML
    private void odaberiScholar() {
        postaviOdabraniTipRadnika(TipRadnika.SCHOLAR);
    }

    @FXML
    private void odaberiScientist() {
        postaviOdabraniTipRadnika(TipRadnika.SCIENTIST);
    }

    private void postaviOdabraniTipRadnika(TipRadnika tipRadnika) {
        odabraniTipRadnika = tipRadnika;
        labelaPorukaPoteza.setText("Odabran radnik: " + tipRadnika);
    }

    @FXML
    private void odigrajPotez() {
        if (reviveEngine.jeIgraZavrsena()) {
            labelaPorukaPoteza.setText("Igra je vec zavrsena.");
            return;
        }

        int odabraniIndeksKarte = listaRukaKarata.getSelectionModel().getSelectedIndex();
        if (odabraniIndeksKarte < 0) {
            labelaPorukaPoteza.setText("Prvo odaberi kartu iz ruke.");
            return;
        }

        if (odabraniTipRadnika == null) {
            labelaPorukaPoteza.setText("Prvo odaberi tip radnika.");
            return;
        }

        Igrac igracNaPotezu = reviveEngine.getIgracNaPotezu();
        Karta odabranaKarta = igracNaPotezu.getRukaKarata().get(odabraniIndeksKarte);
        int brojRundePrijePoteza = reviveEngine.getTrenutnaRunda();
        String imeIgracaPrijePoteza = igracNaPotezu.getImeIgraca();

        RezultatPoteza rezultatPoteza = reviveEngine.izvrsiPotez(odabranaKarta, odabraniTipRadnika);
        labelaPorukaPoteza.setText(rezultatPoteza.getPoruka());

        if (rezultatPoteza.isUspjesno()) {
            zapisPoteza.zapisiPotez(brojRundePrijePoteza, imeIgracaPrijePoteza, odabranaKarta, odabraniTipRadnika);
            PoljePermafrosta otopljenoPolje = rezultatPoteza.getOtopljenoPolje();
            if (otopljenoPolje != null) {
                Rectangle pravokutnikZaAnimaciju = pravokutniciPermafrosta[otopljenoPolje.getRedak()][otopljenoPolje.getStupac()];
                new AnimacijaTopljenja().pokreniAnimacijuNaPolju(pravokutnikZaAnimaciju);
            }
            odabraniTipRadnika = null;
        }

        azurirajSucelje();
    }

    @FXML
    private void zavrsiPotez() {
        AsinkroniZadaci asinkroniZadaci = new AsinkroniZadaci();
        Task<Void> zadatakObrade = asinkroniZadaci.stvoriZadatakSimulacijeObrade();

        zadatakObrade.setOnSucceeded(dogadjaj -> Platform.runLater(this::zavrsiPotezNaGlavnojNiti));

        asinkroniZadaci.pokreniZadatakUPozadini(zadatakObrade);
    }

    private void zavrsiPotezNaGlavnojNiti() {
        reviveEngine.zavrsiPotezIPredajSljedecem();
        odabraniTipRadnika = null;

        if (reviveEngine.jeIgraZavrsena()) {
            zapisPoteza.zavrsiZapis();
            prikaziKrajIgre();
        }

        azurirajSucelje();
    }

    private void prikaziKrajIgre() {
        Bodovanje bodovanje = new Bodovanje();
        Igrac pobjednik = bodovanje.pronadjiPobjednika(reviveEngine.getIgraci());
        String tekstRezultata = bodovanje.formatirajKonacneRezultate(reviveEngine.getIgraci());

        labelaKrajIgre.setText("Igra zavrsena! Pobjednik: " + pobjednik.getImeIgraca() + "\n" + tekstRezultata);
    }

    private void azurirajSucelje() {
        azuriranjeSucelja.azurirajCijeloSucelje(reviveEngine, labelaTrenutniIgrac, labelaRunda,
                listaRukaKarata, labelaResursiIgrac1, labelaResursiIgrac2);
    }

    @FXML
    private void spremiIgru() {
        pomocneAkcijeKontrolera.spremiIgru(reviveEngine, labelaPorukaPoteza);
    }

    @FXML
    private void ucitajIgru() {
        pomocneAkcijeKontrolera.ucitajIgru(labelaPorukaPoteza);
        azurirajSucelje();
    }

    @FXML
    private void prikaziKatalogRadnika() {
        pomocneAkcijeKontrolera.prikaziKatalogRadnika();
    }

    @FXML
    private void prikaziReplay() {
        pomocneAkcijeKontrolera.prikaziReplay();
    }
}