package hr.tvz.revive.kontroler;

import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.PodatakONagradi;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.engine.RezultatPoteza;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipRadnika;
import hr.tvz.revive.xml.ZapisPoteza;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.List;

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
    private ListView<String> listaKarataIgrac1;
    @FXML
    private ListView<String> listaKarataIgrac2;
    @FXML
    private GridPane mrezaPermafrosta;
    @FXML
    private Pane slojPoruka;
    @FXML
    private Button gumbExplorer;
    @FXML
    private Button gumbBuilder;
    @FXML
    private Button gumbScholar;
    @FXML
    private Button gumbScientist;
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
        String imeIgracaNaPotezu = reviveEngine.getIgracNaPotezu().getImeIgraca();
        RezultatPoteza rezultatPoteza = reviveEngine.odigrajKartu(odabranaKarta);
        if (rezultatPoteza.isUspjesno()) {
            zapisPoteza.zapisiPotezKarte(reviveEngine.getTrenutnaRunda(), imeIgracaNaPotezu, odabranaKarta);
        }
        obradiRezultat(rezultatPoteza);
    }

    @FXML
    private void odaberiExplorer() {
        postaviOdabraniTip(TipRadnika.EXPLORER);
    }
    @FXML
    private void odaberiBuilder() {
        postaviOdabraniTip(TipRadnika.BUILDER);
    }
    @FXML
    private void odaberiScholar() {
        postaviOdabraniTip(TipRadnika.SCHOLAR);
    }
    @FXML
    private void odaberiScientist() {
        postaviOdabraniTip(TipRadnika.SCIENTIST);
    }

    private void postaviOdabraniTip(TipRadnika tip) {
        odabraniTipRadnika = tip;
        labelaOdabranaAkcija.setText(tip + " odabran - klikni slobodno polje na ploci.");
    }

    private void kliknutoPolje(int redak, int stupac) {
        if (odabraniTipRadnika == null) {
            labelaPorukaPoteza.setText("Prvo odaberi tip radnika desno.");
            return;
        }
        TipRadnika tipRadnika = odabraniTipRadnika;
        String imeIgracaNaPotezu = reviveEngine.getIgracNaPotezu().getImeIgraca();
        RezultatPoteza rezultatPoteza = reviveEngine.postaviRadnika(tipRadnika, redak, stupac);
        odabraniTipRadnika = null;
        if (rezultatPoteza.isUspjesno()) {
            pomocneAkcijeKontrolera.pokreniAnimacijuPostavljanja(rezultatPoteza, pravokutniciPermafrosta);
            zapisPoteza.zapisiPostavljanjeRadnika(reviveEngine.getTrenutnaRunda(), imeIgracaNaPotezu, tipRadnika);
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
        List<PodatakONagradi> nagrade = reviveEngine.zavrsiPotezIPredajSljedecem();
        pomocneAkcijeKontrolera.prikaziPlutajucePoruke(nagrade, slojPoruka, pravokutniciPermafrosta);
        if (reviveEngine.jeIgraZavrsena()) {
            zapisPoteza.zavrsiZapis();
            pomocneAkcijeKontrolera.prikaziKrajIgre(reviveEngine, this::pokreniNovuPartiju, this::ucitajIgru);
        }
        azurirajSucelje();
    }

    private void azurirajSucelje() {
        azuriranjeSucelja.azurirajCijeloSucelje(reviveEngine, labelaTrenutniIgrac, labelaRunda,
                listaKarataIgrac1, listaKarataIgrac2, labelaStanjeIgrac1, labelaStanjeIgrac2);
        azuriranjeSucelja.azurirajBojePolja(reviveEngine.getPermafrostPloca(), pravokutniciPermafrosta);
        azuriranjeSucelja.azurirajGumbeRadnika(reviveEngine.getIgracNaPotezu(),
                gumbExplorer, gumbBuilder, gumbScholar, gumbScientist);
    }

    @FXML
    private void spremiIgru() {
        pomocneAkcijeKontrolera.spremiIgru(reviveEngine, labelaPorukaPoteza);
    }

    private void ucitajIgru() {
        pomocneAkcijeKontrolera.ucitajIgru(reviveEngine, labelaPorukaPoteza);
        azurirajSucelje();
    }
}