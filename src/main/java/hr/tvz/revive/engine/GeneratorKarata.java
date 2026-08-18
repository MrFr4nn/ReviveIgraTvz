package hr.tvz.revive.engine;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipAkcijeKarte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorKarata {

    public List<Karta> generirajSpil() {
        List<Karta> spil = new ArrayList<>();

        spil.add(new Karta("Zaliha hrane (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 2));
        spil.add(new Karta("Sanduk hrane (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 1));
        spil.add(new Karta("Velika zaliha hrane (za Scholar)", TipAkcijeKarte.DAJ_HRANU, 3));
        spil.add(new Karta("Sanduk zupcanika (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 2));
        spil.add(new Karta("Velik sanduk zupcanika (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 3));
        spil.add(new Karta("Mali sanduk zupcanika (za Builder)", TipAkcijeKarte.DAJ_ZUPCANIKE, 1));
        spil.add(new Karta("Zapisano otkrice (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 2));
        spil.add(new Karta("Vazno otkrice (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 3));
        spil.add(new Karta("Sitno otkrice (za Scientist)", TipAkcijeKarte.DAJ_BODOVE, 1));
        spil.add(new Karta("Energetski kristal (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Veliki kristal (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 2));
        spil.add(new Karta("Mali kristal (za Explorer)", TipAkcijeKarte.DAJ_KRISTAL, 1));

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