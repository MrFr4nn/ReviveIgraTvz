package hr.tvz.revive.engine;

import hr.tvz.revive.model.Igrac;
import java.util.List;

public class Bodovanje {

    public Igrac pronadjiPobjednika(List<Igrac> igraci) {
        Igrac pobjednik = null;
        int najviseBodova = -1;

        for (Igrac igrac : igraci) {
            int ukupnoBodova = igrac.izracunajUkupneBodoveNaKraju();
            if (ukupnoBodova > najviseBodova) {
                najviseBodova = ukupnoBodova;
                pobjednik = igrac;
            }
        }

        return pobjednik;
    }
}