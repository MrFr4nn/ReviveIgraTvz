package hr.tvz.revive.engine;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipAkcijeKarte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generira spil karata za igru Revive. Karte su neovisan izvor
 * resursa (nisu vezane uz radnika) - odigravanjem karte igrac
 * odmah dobiva izravan resurs.
 */
public class GeneratorKarata {

    public List<Karta> generirajSpil() {
        List<Karta> spil = new ArrayList<>();

        spil.add(new Karta("Vrećica hrane", TipAkcijeKarte.DAJ_HRANU, 1));
        spil.add(new Karta("Mali sanduk hrane", TipAkcijeKarte.DAJ_HRANU, 2));
        spil.add(new Karta("Veliki sanduk hrane", TipAkcijeKarte.DAJ_HRANU, 3));
        spil.add(new Karta("Mali sanduk zupčanika", TipAkcijeKarte.DAJ_ZUPCANIKE, 2));
        spil.add(new Karta("Veliki sanduk zupčanika", TipAkcijeKarte.DAJ_ZUPCANIKE, 3));
        spil.add(new Karta("Mali sanduk zupčanika", TipAkcijeKarte.DAJ_ZUPCANIKE, 1));
        spil.add(new Karta("Energetski kristal", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Mali kristal", TipAkcijeKarte.DAJ_KRISTAL, 1));
        spil.add(new Karta("Veliki kristal", TipAkcijeKarte.DAJ_KRISTAL, 2));
        spil.add(new Karta("Stečena znanja", TipAkcijeKarte.DAJ_BODOVE, 2));
        spil.add(new Karta("Važan zapis iz starog doba", TipAkcijeKarte.DAJ_BODOVE, 3));
        spil.add(new Karta("Pronalazak malog sanduka blaga", TipAkcijeKarte.DAJ_BODOVE, 1));

        Collections.shuffle(spil);
        return spil;
    }

    public List<Karta> izvuciKarte(List<Karta> spil, int brojKarata) {
        List<Karta> izvucene = new ArrayList<>();
        for (int i = 0; i < brojKarata; i++) {
            if (!spil.isEmpty()) {
                izvucene.add(spil.remove(0));
            }
        }
        return izvucene;
    }
}