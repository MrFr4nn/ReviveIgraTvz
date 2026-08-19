package hr.tvz.revive.kontroler;

import hr.tvz.revive.animacija.AnimacijaTopljenja;
import hr.tvz.revive.animacija.PlutajucaPoruka;
import hr.tvz.revive.engine.Bodovanje;
import hr.tvz.revive.engine.PodatakONagradi;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class PomocneAkcijeKontrolera {

    private static final String PUTANJA_XML_DNEVNIKA = "revive-log.xml";
    private static final String PUTANJA_REPLAY_FXML = "/hr/tvz/revive/replay-ekran.fxml";
    private static final String PUTANJA_KATALOG_FXML = "/hr/tvz/revive/katalog-ekran.fxml";
    private static final String PUTANJA_KRAJ_IGRE_FXML = "/hr/tvz/revive/kraj-igre-ekran.fxml";

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
        try {
            KatalogRadnikaGenerator katalogRadnikaGenerator = new KatalogRadnikaGenerator();
            FXMLLoader ucitavacFxml = new FXMLLoader(getClass().getResource(PUTANJA_KATALOG_FXML));
            Parent korijenskiElement = ucitavacFxml.load();

            KontrolerKatalog kontrolerKatalog = ucitavacFxml.getController();
            kontrolerKatalog.postaviTekst(katalogRadnikaGenerator.generirajKatalog());

            Stage prozorKataloga = new Stage();
            prozorKataloga.setTitle("Reflection katalog");
            prozorKataloga.setScene(new Scene(korijenskiElement));
            prozorKataloga.show();
        } catch (IOException iznimka) {
            System.out.println("Greska prilikom otvaranja kataloga: " + iznimka.getMessage());
        }
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

    public void prikaziKrajIgre(ReviveEngine reviveEngine, Runnable akcijaIgrajPonovno) {
        try {
            Bodovanje bodovanje = new Bodovanje();
            Igrac pobjednik = bodovanje.pronadjiPobjednika(reviveEngine.getIgraci());
            String tekstRezultata = formatirajRezultate(reviveEngine.getIgraci());

            FXMLLoader ucitavacFxml = new FXMLLoader(getClass().getResource(PUTANJA_KRAJ_IGRE_FXML));
            Parent korijenskiElement = ucitavacFxml.load();

            KontrolerKrajIgre kontrolerKrajIgre = ucitavacFxml.getController();
            kontrolerKrajIgre.postaviPodatke("Pobjednik: " + pobjednik.getImeIgraca(), tekstRezultata,
                    akcijaIgrajPonovno, this::prikaziReplay);

            Stage prozorKrajaIgre = new Stage();
            prozorKrajaIgre.setTitle("Igra zavrsena");
            prozorKrajaIgre.setScene(new Scene(korijenskiElement));
            prozorKrajaIgre.show();
        } catch (IOException iznimka) {
            System.out.println("Greska prilikom otvaranja ekrana kraja igre: " + iznimka.getMessage());
        }
    }

    private String formatirajRezultate(List<Igrac> igraci) {
        StringBuilder tekst = new StringBuilder();
        for (Igrac igrac : igraci) {
            tekst.append(igrac.getImeIgraca()).append(": ")
                    .append(igrac.izracunajUkupneBodoveNaKraju()).append(" bodova\n");
        }
        return tekst.toString();
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

    public void prikaziPlutajucePoruke(List<PodatakONagradi> nagrade, Pane slojPoruka, Rectangle[][] pravokutnici) {
        PlutajucaPoruka plutajucaPoruka = new PlutajucaPoruka();
        for (PodatakONagradi nagrada : nagrade) {
            Rectangle pravokutnik = pravokutnici[nagrada.getRedak()][nagrada.getStupac()];
            plutajucaPoruka.prikaziPoruku(slojPoruka, nagrada.getTekstNagrade(),
                    pravokutnik.getX(), pravokutnik.getY() - 20);
        }
    }
}