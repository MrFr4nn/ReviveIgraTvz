package hr.tvz.revive.kontroler;

import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.PodatakONagradi;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.xml.ZapisPoteza;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.List;

public class ObradaPotezaKontrolera {

    private final ReviveEngine reviveEngine;
    private final ZapisPoteza zapisPoteza;
    private final AzuriranjeSucelja azuriranjeSucelja;
    private final PomocneAkcijeKontrolera pomocneAkcijeKontrolera;
    private final Pane slojPoruka;
    private final Rectangle[][] pravokutniciPermafrosta;
    private final Node referentniNode;
    private final Button gumbOdigrajKartu;
    private final Button gumbPredajPotez;
    private final Button gumbExplorer;
    private final Button gumbBuilder;
    private final Button gumbScholar;
    private final Button gumbScientist;

    public ObradaPotezaKontrolera(ReviveEngine reviveEngine, ZapisPoteza zapisPoteza,
                                  AzuriranjeSucelja azuriranjeSucelja, PomocneAkcijeKontrolera pomocneAkcijeKontrolera,
                                  Pane slojPoruka, Rectangle[][] pravokutniciPermafrosta, Node referentniNode,
                                  Button gumbOdigrajKartu, Button gumbPredajPotez,
                                  Button gumbExplorer, Button gumbBuilder, Button gumbScholar, Button gumbScientist) {
        this.reviveEngine = reviveEngine;
        this.zapisPoteza = zapisPoteza;
        this.azuriranjeSucelja = azuriranjeSucelja;
        this.pomocneAkcijeKontrolera = pomocneAkcijeKontrolera;
        this.slojPoruka = slojPoruka;
        this.pravokutniciPermafrosta = pravokutniciPermafrosta;
        this.referentniNode = referentniNode;
        this.gumbOdigrajKartu = gumbOdigrajKartu;
        this.gumbPredajPotez = gumbPredajPotez;
        this.gumbExplorer = gumbExplorer;
        this.gumbBuilder = gumbBuilder;
        this.gumbScholar = gumbScholar;
        this.gumbScientist = gumbScientist;
    }

    public void pokreniAsinkronuObraduPoteza(Runnable naZavrsetku) {
        azuriranjeSucelja.postaviOnemogucenostAkcija(true, pravokutniciPermafrosta,
                gumbOdigrajKartu, gumbPredajPotez, gumbExplorer, gumbBuilder, gumbScholar, gumbScientist);

        AsinkroniZadaci asinkroniZadaci = new AsinkroniZadaci();
        Task<Void> zadatakObrade = asinkroniZadaci.stvoriZadatakSimulacijeObrade();
        zadatakObrade.setOnSucceeded(dogadjaj ->
                Platform.runLater(() -> zavrsiPotezNaGlavnojNiti(naZavrsetku)));
        asinkroniZadaci.pokreniZadatakUPozadini(zadatakObrade);
    }

    public void omoguciSveAkcije() {
        azuriranjeSucelja.postaviOnemogucenostAkcija(false, pravokutniciPermafrosta,
                gumbOdigrajKartu, gumbPredajPotez, gumbExplorer, gumbBuilder, gumbScholar, gumbScientist);
    }

    private void zavrsiPotezNaGlavnojNiti(Runnable naZavrsetku) {
        int rundaPrijeObradeNagrada = reviveEngine.getTrenutnaRunda();
        List<PodatakONagradi> nagrade = reviveEngine.zavrsiPotezIPredajSljedecem();
        pomocneAkcijeKontrolera.prikaziPlutajucePoruke(nagrade, slojPoruka, pravokutniciPermafrosta);
        zapisiNagradeUDnevnik(rundaPrijeObradeNagrada, nagrade);
        naZavrsetku.run();
    }

    private void zapisiNagradeUDnevnik(int brojRunde, List<PodatakONagradi> nagrade) {
        for (PodatakONagradi nagrada : nagrade) {
            String imeIgraca = reviveEngine.getIgraci().get(nagrada.getIndeksVlasnika()).getImeIgraca();
            zapisPoteza.zapisiNagradu(brojRunde, imeIgraca, nagrada.getTekstNagrade());
        }
    }

    public Stage dohvatiGlavniProzor() {
        return (Stage) referentniNode.getScene().getWindow();
    }
}