package hr.tvz.revive.kontroler;

import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.engine.RezultatPoteza;
import hr.tvz.revive.model.Karta;
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
    private Label labelaStanjeIgrac1;

    @FXML
    private Label labelaStanjeIgrac2;

    @FXML
    private Label labelaOdabranaAkcija;

    @FXML
    private Label labelaKrajIgre;

    @FXML
    private ListView<String> listaKarataIgrac1;

    @FXML
    private ListView<String> listaKarataIgrac2;

    @FXML
    private GridPane mrezaPermafrosta;

    private ReviveEngine reviveEngine;
    private ZapisPoteza zapisPoteza;
    private AzuriranjeSucelja azuriranjeSucelja;
    private PomocneAkcijeKontrolera pomocneAkcijeKontrolera;
    private Rectangle[][] pravokutniciPermafrosta;
    private TipRadnika odabraniTipRadnika;

    @FXML
    public void initialize() {
        reviveEngine = new ReviveEngine();
        azuriranjeSucelja = new AzuriranjeSucelja();
        pomocneAkcijeKontrolera = new PomocneAkcijeKontrolera();
        pravokutniciPermafrosta = azuriranjeSucelja.izgradiPermafrostMrezu(mrezaPermafrosta);
        azuriranjeSucelja.postaviKlikoveNaPolja(pravokutniciPermafrosta, this::kliknutoPolje);
        pokreniNovuPartiju();
    }

    private void pokreniNovuPartiju() {
        zapisPoteza = new ZapisPoteza();
        reviveEngine.pokreniNovuIgru("Igrac 1", "Igrac 2");
        zapisPoteza.zapocniZapis();
        odabraniTipRadnika = null;
        labelaKrajIgre.setText("");
        labelaPorukaPoteza.setText("");
        azurirajSucelje();
    }

    @FXML
    private void odigrajOdabranuKartu() {
        ListView<String> listaAktivna = reviveEngine.getIndeksIgracaNaPotezu() == 0 ? listaKarataIgrac1 : listaKarataIgrac2;
        int indeks = listaAktivna.getSelectionModel().getSelectedIndex();
        if (indeks < 0) {
            labelaPorukaPoteza.setText("Prvo odaberi kartu iz svoje ruke.");
            return;
        }
        Karta odabranaKarta = reviveEngine.getIgracNaPotezu().getRukaKarata().get(indeks);
        RezultatPoteza rezultatPoteza = reviveEngine.odigrajKartu(odabranaKarta);
        obradiRezultat(rezultatPoteza);
    }

    @FXML
    private void odaberiExplorer() {
        odabraniTipRadnika = TipRadnika.EXPLORER;
        labelaOdabranaAkcija.setText("Explorer odabran - klikni slobodno polje na ploci.");
    }

    @FXML
    private void odaberiBuilder() {
        odabraniTipRadnika = TipRadnika.BUILDER;
        labelaOdabranaAkcija.setText("Builder odabran - klikni slobodno polje na ploci.");
    }

    @FXML
    private void odaberiScholar() {
        odabraniTipRadnika = TipRadnika.SCHOLAR;
        labelaOdabranaAkcija.setText("Scholar odabran - klikni slobodno polje na ploci.");
    }

    @FXML
    private void odaberiScientist() {
        odabraniTipRadnika = TipRadnika.SCIENTIST;
        labelaOdabranaAkcija.setText("Scientist odabran - klikni slobodno polje na ploci.");
    }

    private void kliknutoPolje(int redak, int stupac) {
        if (odabraniTipRadnika == null) {
            labelaPorukaPoteza.setText("Prvo odaberi tip radnika desno.");
            return;
        }
        RezultatPoteza rezultatPoteza = reviveEngine.postaviRadnika(odabraniTipRadnika, redak, stupac);
        odabraniTipRadnika = null;
        if (rezultatPoteza.isUspjesno()) {
            pomocneAkcijeKontrolera.pokreniAnimacijuPostavljanja(rezultatPoteza, pravokutniciPermafrosta);
        }
        obradiRezultat(rezultatPoteza);
    }

    private void obradiRezultat(RezultatPoteza rezultatPoteza) {
        labelaPorukaPoteza.setText(rezultatPoteza.getPoruka());
        if (rezultatPoteza.isUspjesno()) {
            pokreniAsinkronuPredajuPoteza();
        } else {
            azurirajSucelje();
        }
    }

    private void pokreniAsinkronuPredajuPoteza() {
        AsinkroniZadaci asinkroniZadaci = new AsinkroniZadaci();
        Task<Void> zadatakObrade = asinkroniZadaci.stvoriZadatakSimulacijeObrade();
        zadatakObrade.setOnSucceeded(dogadjaj -> Platform.runLater(this::zavrsiPotezNaGlavnojNiti));
        asinkroniZadaci.pokreniZadatakUPozadini(zadatakObrade);
    }

    private void zavrsiPotezNaGlavnojNiti() {
        reviveEngine.zavrsiPotezIPredajSljedecem();
        if (reviveEngine.jeIgraZavrsena()) {
            zapisPoteza.zavrsiZapis();
            pomocneAkcijeKontrolera.prikaziKrajIgre(reviveEngine, labelaKrajIgre);
        }
        azurirajSucelje();
    }

    private void azurirajSucelje() {
        azuriranjeSucelja.azurirajCijeloSucelje(reviveEngine, labelaTrenutniIgrac, labelaRunda,
                listaKarataIgrac1, listaKarataIgrac2, labelaStanjeIgrac1, labelaStanjeIgrac2);
        azuriranjeSucelja.azurirajBojePolja(reviveEngine.getPermafrostPloca(), pravokutniciPermafrosta);
    }

    @FXML
    private void spremiIgru() {
        pomocneAkcijeKontrolera.spremiIgru(reviveEngine, labelaPorukaPoteza);
    }

    @FXML
    private void ucitajIgru() {
        pomocneAkcijeKontrolera.ucitajIgru(reviveEngine, labelaPorukaPoteza);
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

    @FXML
    private void igrajPonovno() {
        pokreniNovuPartiju();
    }
}