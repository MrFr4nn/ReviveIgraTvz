package hr.tvz.revive.engine;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipAkcijeKarte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorKarata {

    public List<Karta> generirajSpil() {
        List<Karta> spil = new ArrayList<>();

        spil.add(new Karta("Lovac", TipAkcijeKarte.DAJ_HRANU, 2));
        spil.add(new Karta("Sakupljac", TipAkcijeKarte.DAJ_HRANU, 1));
        spil.add(new Karta("Ribar", TipAkcijeKarte.DAJ_HRANU, 3));
        spil.add(new Karta("Kovac", TipAkcijeKarte.DAJ_ZUPCANIKE, 2));
        spil.add(new Karta("Rudar", TipAkcijeKarte.DAJ_ZUPCANIKE, 3));
        spil.add(new Karta("Mehanicar", TipAkcijeKarte.DAJ_ZUPCANIKE, 1));
        spil.add(new Karta("Ucenjak", TipAkcijeKarte.DAJ_BODOVE, 2));
        spil.add(new Karta("Vodja Plemena", TipAkcijeKarte.DAJ_BODOVE, 3));
        spil.add(new Karta("Cuvar Znanja", TipAkcijeKarte.DAJ_BODOVE, 1));
        spil.add(new Karta("Tragac Kristala", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Istrazivac", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Graditelj", TipAkcijeKarte.DAJ_BODOVE, 2));

        Collections.shuffle(spil);
        return spil;
    }

    public List<Karta> podijeliPocetnuRuku(List<Karta> spil, int brojKarata) {
        List<Karta> pocetnaRuka = new ArrayList<>();
        for (int i = 0; i < brojKarata; i++) {
            if (!spil.isEmpty()) {
                pocetnaRuka.add(spil.remove(0));
            }
        }
        return pocetnaRuka;
    }
}