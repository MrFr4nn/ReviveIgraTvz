package hr.tvz.revive.engine;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipAkcijeKarte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorKarata {

    public List<Karta> generirajSpil() {
        List<Karta> spil = new ArrayList<>();

        spil.add(new Karta("Ucenik (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 2));
        spil.add(new Karta("Knjiznicar (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 1));
        spil.add(new Karta("Mentor (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 3));
        spil.add(new Karta("Kovac (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 2));
        spil.add(new Karta("Rudar (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 3));
        spil.add(new Karta("Mehanicar (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 1));
        spil.add(new Karta("Ucenjak (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 2));
        spil.add(new Karta("Vodja Plemena (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 3));
        spil.add(new Karta("Cuvar Znanja (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 1));
        spil.add(new Karta("Tragac Kristala (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Istrazivac (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 2));
        spil.add(new Karta("Pustolov (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 1));

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