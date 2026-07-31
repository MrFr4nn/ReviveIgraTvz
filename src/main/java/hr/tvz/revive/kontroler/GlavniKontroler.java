package hr.tvz.revive.kontroler;

import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.engine.RezultatPoteza;
import hr.tvz.revive.model.Igrac;
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
    private Label labelaResursiIgrac1;

    @FXML
    private Label labelaResursiIgrac2;

    @FXML
    private Label labelaStatusRadnika;

    @FXML
    private Label labelaOdabranaAkcija;

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
    private Rectangle[][] pravokutniciPermafrosta;

    @FXML
    public void initialize() {
        reviveEngine = new ReviveEngine();
        zapisPoteza = new ZapisPoteza();
        azuriranjeSucelja = new AzuriranjeSucelja();
        pomocneAkcijeKontrolera = new PomocneAkcijeKontrolera();
        reviveEngine.pokreniNovuIgru("Igrac 1", "Igrac 2");
        zapisPoteza.zapocniZapis();

        pravokutniciPermafrosta = azuriranjeSucelja.izgradiPermafrostMrezu(mrezaPermafrosta);
        azuriranjeSucelja.postaviKlikoveNaPolja(pravokutniciPermafrosta, this::odigrajPotezNaPolju);
        listaRukaKarata.getSelectionModel().selectedIndexProperty().addListener((obs, staro, novo) -> prikaziOdabranuAkciju());
        azurirajSucelje();
    }

    private void prikaziOdabranuAkciju() {
        Karta odabranaKarta = dohvatiOdabranuKartu();
        if (odabranaKarta == null) {
            labelaOdabranaAkcija.setText("Odaberi kartu iz ruke da vidis koji radnik ide uz nju.");
            return;
        }
        TipRadnika potrebniTip = odabranaKarta.getTipAkcije().getPovezaniTipRadnika();
        if (potrebniTip == TipRadnika.EXPLORER) {
            labelaOdabranaAkcija.setText("Karta zahtijeva: " + potrebniTip + " - klikni Permafrost polje da odigras potez.");
        } else {
            labelaOdabranaAkcija.setText("Karta zahtijeva: " + potrebniTip + " - klikni 'Odigraj potez'.");
        }
    }

    private Karta dohvatiOdabranuKartu() {
        int odabraniIndeks = listaRukaKarata.getSelectionModel().getSelectedIndex();
        if (odabraniIndeks < 0) {
            return null;
        }
        return reviveEngine.getIgracNaPotezu().getRukaKarata().get(odabraniIndeks);
    }

    private void odigrajPotezNaPolju(int redak, int stupac) {
        Karta odabranaKarta = dohvatiOdabranuKartu();
        if (odabranaKarta != null && odabranaKarta.getTipAkcije().getPovezaniTipRadnika() == TipRadnika.EXPLORER) {
            odigrajPotez(redak, stupac);
        }
    }

    @FXML
    private void odigrajPotez() {
        odigrajPotez(-1, -1);
    }

    private void odigrajPotez(int redakPolja, int stupacPolja) {
        if (reviveEngine.jeIgraZavrsena()) {
            labelaPorukaPoteza.setText("Igra je vec zavrsena.");
            return;
        }

        Karta odabranaKarta = dohvatiOdabranuKartu();
        if (odabranaKarta == null) {
            labelaPorukaPoteza.setText("Prvo odaberi kartu iz ruke.");
            return;
        }

        TipRadnika tipRadnika = odabranaKarta.getTipAkcije().getPovezaniTipRadnika();
        Igrac igracNaPotezu = reviveEngine.getIgracNaPotezu();
        int brojRundePrijePoteza = reviveEngine.getTrenutnaRunda();
        String imeIgracaPrijePoteza = igracNaPotezu.getImeIgraca();

        RezultatPoteza rezultatPoteza = reviveEngine.izvrsiPotez(odabranaKarta, tipRadnika, redakPolja, stupacPolja);
        labelaPorukaPoteza.setText(rezultatPoteza.getPoruka());

        if (rezultatPoteza.isUspjesno()) {
            zapisPoteza.zapisiPotez(brojRundePrijePoteza, imeIgracaPrijePoteza, odabranaKarta, tipRadnika);
            pomocneAkcijeKontrolera.pokreniAnimacijuAkoJePoljeOtopljeno(rezultatPoteza, pravokutniciPermafrosta);
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

        if (reviveEngine.jeIgraZavrsena()) {
            zapisPoteza.zavrsiZapis();
            pomocneAkcijeKontrolera.prikaziKrajIgre(reviveEngine, labelaKrajIgre);
        }

        azurirajSucelje();
    }

    private void azurirajSucelje() {
        azuriranjeSucelja.azurirajCijeloSucelje(reviveEngine, labelaTrenutniIgrac, labelaRunda,
                listaRukaKarata, labelaResursiIgrac1, labelaResursiIgrac2, labelaStatusRadnika);
        prikaziOdabranuAkciju();
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