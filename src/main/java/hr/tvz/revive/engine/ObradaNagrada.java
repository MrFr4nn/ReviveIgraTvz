package hr.tvz.revive.engine;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.Masina;
import hr.tvz.revive.model.Radnik;

import java.util.List;
import java.util.Random;

public class ObradaNagrada {

    private static final int NAGRADA_PO_RUNDI = 2;
    private static final double SANSA_ZA_KARTU = 0.50;

    private Random nasumicniGenerator = new Random();

    public String dodijeliNagraduRadnika(Igrac igrac, Radnik radnik, List<Karta> spilKarata) {
        switch (radnik.getTip()) {
            case EXPLORER:
                return dodijeliNagraduExplorera(igrac, spilKarata);
            case BUILDER:
                igrac.getIzgradjeneMasine().add(new Masina());
                return "+1 Mašina";
            case SCHOLAR:
                igrac.dodajHranu(NAGRADA_PO_RUNDI);
                return "+" + NAGRADA_PO_RUNDI + " hrane";
            case SCIENTIST:
                igrac.dodajBodove(NAGRADA_PO_RUNDI);
                return "+" + NAGRADA_PO_RUNDI + " bodova";
            default:
                return "";
        }
    }

    private String dodijeliNagraduExplorera(Igrac igrac, List<Karta> spilKarata) {
        if (nasumicniGenerator.nextDouble() < SANSA_ZA_KARTU && !spilKarata.isEmpty()) {
            igrac.getRukaKarata().add(spilKarata.remove(0));
            return "Nova karta!";
        }
        igrac.dodajKristale(NAGRADA_PO_RUNDI);
        return "+" + NAGRADA_PO_RUNDI + " kristala";
    }
}