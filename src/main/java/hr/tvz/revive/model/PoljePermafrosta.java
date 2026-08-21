package hr.tvz.revive.model;

import java.io.Serializable;

public class PoljePermafrosta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int redak;
    private int stupac;
    private boolean zauzeto;
    private int indeksVlasnika;

    public PoljePermafrosta(int redak, int stupac) {
        this.redak = redak;
        this.stupac = stupac;
        this.zauzeto = false;
        this.indeksVlasnika = -1;
    }

    public int getRedak() {
        return redak;
    }

    public int getStupac() {
        return stupac;
    }

    public boolean isZauzeto() {
        return zauzeto;
    }

    public int getIndeksVlasnika() {
        return indeksVlasnika;
    }

    public void zauzmi(int indeksVlasnika) {
        this.zauzeto = true;
        this.indeksVlasnika = indeksVlasnika;
    }
}