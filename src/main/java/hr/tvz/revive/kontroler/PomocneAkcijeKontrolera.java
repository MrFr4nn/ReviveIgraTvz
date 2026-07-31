package hr.tvz.revive.kontroler;

import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.model.StanjeIgre;
import hr.tvz.revive.reflection.KatalogRadnikaGenerator;
import hr.tvz.revive.serijalizacija.SpremanjeIgre;
import hr.tvz.revive.xml.ReplaySustav;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class PomocneAkcijeKontrolera {

    private static final String PUTANJA_XML_DNEVNIKA = "revive-log.xml";

    public void spremiIgru(ReviveEngine reviveEngine, Label labelaPorukaPoteza) {
        StanjeIgre stanjeIgre = new StanjeIgre(reviveEngine.getIgraci(), reviveEngine.getPermafrostPloca(),
                reviveEngine.getSpilKarata(), reviveEngine.getTrenutnaRunda(), reviveEngine.getIndeksIgracaNaPotezu());

        SpremanjeIgre spremanjeIgre = new SpremanjeIgre();
        boolean uspjesnoSpremljeno = spremanjeIgre.spremiIgru(stanjeIgre);

        if (uspjesnoSpremljeno) {
            labelaPorukaPoteza.setText("Igra je uspjesno spremljena.");
        } else {
            labelaPorukaPoteza.setText("Greska prilikom spremanja igre.");
        }
    }

    public StanjeIgre ucitajIgru(Label labelaPorukaPoteza) {
        SpremanjeIgre spremanjeIgre = new SpremanjeIgre();
        StanjeIgre ucitanoStanje = spremanjeIgre.ucitajIgru();

        if (ucitanoStanje == null) {
            labelaPorukaPoteza.setText("Nema spremljene igre za ucitati.");
        } else {
            labelaPorukaPoteza.setText("Igra je uspjesno ucitana.");
        }

        return ucitanoStanje;
    }

    public void prikaziKatalogRadnika() {
        KatalogRadnikaGenerator katalogRadnikaGenerator = new KatalogRadnikaGenerator();
        String tekstKataloga = katalogRadnikaGenerator.generirajKatalog();

        prikaziProzorInformacije("Katalog radnika", "Sposobnosti tipova radnika", tekstKataloga);
    }

    public void prikaziReplay() {
        ReplaySustav replaySustav = new ReplaySustav();
        String tekstReplaya = replaySustav.generirajTekstualniReplay(PUTANJA_XML_DNEVNIKA);
        String sadrzajZaPrikaz = tekstReplaya.isEmpty() ? "Nema jos zapisanih poteza." : tekstReplaya;

        prikaziProzorInformacije("Replay odigranih poteza", "Tok igre po rundama", sadrzajZaPrikaz);
    }

    private void prikaziProzorInformacije(String naslov, String zaglavlje, String sadrzaj) {
        Alert prozorInformacije = new Alert(Alert.AlertType.INFORMATION);
        prozorInformacije.setTitle(naslov);
        prozorInformacije.setHeaderText(zaglavlje);
        prozorInformacije.setContentText(sadrzaj);
        prozorInformacije.showAndWait();
    }
}