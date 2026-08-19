package hr.tvz.revive.kontroler;

import hr.tvz.revive.animacija.AnimacijaTopljenja;
import hr.tvz.revive.engine.Bodovanje;
import hr.tvz.revive.engine.ReviveEngine;
import hr.tvz.revive.engine.RezultatPoteza;
import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.StanjeIgre;
import hr.tvz.revive.reflection.KatalogRadnikaGenerator;
import hr.tvz.revive.serijalizacija.SpremanjeIgre;
import hr.tvz.revive.xml.PodatakOPotezu;
import hr.tvz.revive.xml.ReplaySustav;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class PomocneAkcijeKontrolera {

    private static final String PUTANJA_XML_DNEVNIKA = "revive-log.xml";
    private static final String PUTANJA_REPLAY_FXML = "/hr/tvz/revive/replay-ekran.fxml";

    public void spremiIgru(ReviveEngine reviveEngine, Label labelaPorukaPoteza) {
        StanjeIgre stanjeIgre = new StanjeIgre(reviveEngine.getIgraci(), reviveEngine.getPermafrostPloca(),
                reviveEngine.getSpilKarata(), reviveEngine.getTrenutnaRunda(), reviveEngine.getIndeksIgracaNaPotezu());

        SpremanjeIgre spremanjeIgre = new SpremanjeIgre();
        boolean uspjesnoSpremljeno = spremanjeIgre.spremiIgru(stanjeIgre);

        labelaPorukaPoteza.setText(uspjesnoSpremljeno
                ? "Igra je uspjesno spremljena." : "Greska prilikom spremanja igre.");
    }

    public boolean ucitajIgru(ReviveEngine reviveEngine, Label labelaPorukaPoteza) {
        SpremanjeIgre spremanjeIgre = new SpremanjeIgre();
        StanjeIgre ucitanoStanje = spremanjeIgre.ucitajIgru();

        if (ucitanoStanje == null) {
            labelaPorukaPoteza.setText("Nema spremljene igre za ucitati.");
            return false;
        }

        reviveEngine.primijeniUcitanoStanje(ucitanoStanje);
        labelaPorukaPoteza.setText("Igra je uspjesno ucitana.");
        return true;
    }

    public void prikaziKatalogRadnika() {
        KatalogRadnikaGenerator katalogRadnikaGenerator = new KatalogRadnikaGenerator();
        prikaziProzorInformacije("Reflection katalog", "Dinamicka analiza modela igre", katalogRadnikaGenerator.generirajKatalog());
    }

    public void prikaziReplay() {
        try {
            ReplaySustav replaySustav = new ReplaySustav();
            List<PodatakOPotezu> svilPotezi = replaySustav.ucitajSvePotezeRedom(PUTANJA_XML_DNEVNIKA);

            FXMLLoader ucitavacFxml = new FXMLLoader(getClass().getResource(PUTANJA_REPLAY_FXML));
            Parent korijenskiElement = ucitavacFxml.load();

            KontrolerReplay kontrolerReplay = ucitavacFxml.getController();
            kontrolerReplay.postaviPoteze(svilPotezi);

            Stage prozorReplaya = new Stage();
            prozorReplaya.setTitle("Replay odigranih poteza");
            prozorReplaya.setScene(new Scene(korijenskiElement));
            prozorReplaya.show();
        } catch (IOException iznimka) {
            System.out.println("Greska prilikom otvaranja replay prozora: " + iznimka.getMessage());
        }
    }

    public void prikaziKrajIgre(ReviveEngine reviveEngine, Label labelaKrajIgre) {
        Bodovanje bodovanje = new Bodovanje();
        Igrac pobjednik = bodovanje.pronadjiPobjednika(reviveEngine.getIgraci());

        labelaKrajIgre.setText("Igra zavrsena!\nPobjednik: " + pobjednik.getImeIgraca()
                + " (" + pobjednik.izracunajUkupneBodoveNaKraju() + " bodova)");
    }

    public void pokreniAnimacijuPostavljanja(RezultatPoteza rezultatPoteza, Rectangle[][] pravokutnici) {
        PoljePermafrosta postavljenoPolje = rezultatPoteza.getPostavljenoPolje();
        if (postavljenoPolje == null) {
            return;
        }
        Rectangle pravokutnik = pravokutnici[postavljenoPolje.getRedak()][postavljenoPolje.getStupac()];
        Color bojaVlasnika = postavljenoPolje.getIndeksVlasnika() == 0 ? Color.SEAGREEN : Color.DODGERBLUE;
        new AnimacijaTopljenja().pokreniAnimacijuNaPolju(pravokutnik, bojaVlasnika);
    }

    private void prikaziProzorInformacije(String naslov, String zaglavlje, String sadrzaj) {
        Alert prozorInformacije = new Alert(Alert.AlertType.INFORMATION);
        prozorInformacije.setTitle(naslov);
        prozorInformacije.setHeaderText(zaglavlje);
        prozorInformacije.setContentText(sadrzaj);
        prozorInformacije.showAndWait();
    }
}