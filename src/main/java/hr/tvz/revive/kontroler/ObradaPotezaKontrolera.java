package hr.tvz.revive.kontroler;

import hr.tvz.revive.async.AsinkroniZadaci;
import hr.tvz.revive.engine.PodatakONagradi;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.xml.ZapisPoteza;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.List;

public class ObradaPotezaKontrolera {

    private final ReviveEngine reviveEngine;
    private final ZapisPoteza zapisPoteza;
    private final PomocneAkcijeKontrolera pomocneAkcijeKontrolera;
    private final Pane slojPoruka;
    private final Rectangle[][] pravokutniciPermafrosta;
    private final Node referentniNode;

    public ObradaPotezaKontrolera(ReviveEngine reviveEngine, ZapisPoteza zapisPoteza,
                                  PomocneAkcijeKontrolera pomocneAkcijeKontrolera,
                                  Pane slojPoruka, Rectangle[][] pravokutniciPermafrosta, Node referentniNode) {
        this.reviveEngine = reviveEngine;
        this.zapisPoteza = zapisPoteza;
        this.pomocneAkcijeKontrolera = pomocneAkcijeKontrolera;
        this.slojPoruka = slojPoruka;
        this.pravokutniciPermafrosta = pravokutniciPermafrosta;
        this.referentniNode = referentniNode;
    }

    public void pokreniAsinkronuObraduPoteza(Runnable naZavrsetku) {
        AsinkroniZadaci asinkroniZadaci = new AsinkroniZadaci();
        Task<Void> zadatakObrade = asinkroniZadaci.stvoriZadatakSimulacijeObrade();
        zadatakObrade.setOnSucceeded(dogadjaj ->
                Platform.runLater(() -> zavrsiPotezNaGlavnojNiti(naZavrsetku)));
        asinkroniZadaci.pokreniZadatakUPozadini(zadatakObrade);
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