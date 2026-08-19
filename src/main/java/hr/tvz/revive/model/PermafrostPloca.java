package hr.tvz.revive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermafrostPloca implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int VELICINA = 3;

    private List<PoljePermafrosta> polja;

    public PermafrostPloca() {
        polja = new ArrayList<>();
        popuniPlocu();
    }

    private void popuniPlocu() {
        for (int redak = 0; redak < VELICINA; redak++) {
            for (int stupac = 0; stupac < VELICINA; stupac++) {
                polja.add(new PoljePermafrosta(redak, stupac));
            }
        }
    }

    public List<PoljePermafrosta> getPolja() {
        return polja;
    }

    public PoljePermafrosta pronadjiPolje(int redak, int stupac) {
        for (PoljePermafrosta polje : polja) {
            if (polje.getRedak() == redak && polje.getStupac() == stupac) {
                return polje;
            }
        }
        return null;
    }
}