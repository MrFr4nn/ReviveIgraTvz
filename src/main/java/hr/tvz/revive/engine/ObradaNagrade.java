package hr.tvz.revive.engine;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.Masina;
import hr.tvz.revive.model.Radnik;
import java.util.List;
import java.util.Random;


public class ObradaNagrada {

    private static final int NAGRADA_PO_RUNDI = 2;
    private static final double SANSA_ZA_KARTU = 0.34;

    private Random nasumicniGenerator = new Random();

    public void dodijeliNagraduRadnika(Igrac igrac, Radnik radnik, List<Karta> spilKarata) {
        switch (radnik.getTip()) {
            case EXPLORER:
                dodijeliNagraduExplorera(igrac, spilKarata);
                break;
            case BUILDER:
                igrac.getIzgradjeneMasine().add(new Masina("Masina " + (igrac.getIzgradjeneMasine().size() + 1)));
                break;
            case SCHOLAR:
                igrac.dodajHranu(NAGRADA_PO_RUNDI);
                break;
            case SCIENTIST:
                igrac.dodajBodove(NAGRADA_PO_RUNDI);
                break;
            default:
                break;
        }
    }

    private void dodijeliNagraduExplorera(Igrac igrac, List<Karta> spilKarata) {
        if (nasumicniGenerator.nextDouble() < SANSA_ZA_KARTU && !spilKarata.isEmpty()) {
            igrac.getRukaKarata().add(spilKarata.remove(0));
        } else {
            igrac.dodajKristale(NAGRADA_PO_RUNDI);
        }
    }
}