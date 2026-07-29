package hr.tvz.revive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PermafrostPloca implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int VELICINA = 5;

    private List<PoljePermafrosta> polja;

    public PermafrostPloca() {
        polja = new ArrayList<>();
        popuniPlocu();
    }

    private void popuniPlocu() {
        Random nasumicniGenerator = new Random();
        for (int redak = 0; redak < VELICINA; redak++) {
            for (int stupac = 0; stupac < VELICINA; stupac++) {
                int bonus = nasumicniGenerator.nextInt(3) + 1;
                polja.add(new PoljePermafrosta(redak, stupac, bonus));
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

    public PoljePermafrosta pronadjiSljedecePrazamrznutoPolje() {
        for (PoljePermafrosta polje : polja) {
            if (!polje.isOtopljeno()) {
                return polje;
            }
        }
        return null;
    }

    public int brojOtopljenihPolja() {
        int brojac = 0;
        for (PoljePermafrosta polje : polja) {
            if (polje.isOtopljeno()) {
                brojac++;
            }
        }
        return brojac;
    }
}