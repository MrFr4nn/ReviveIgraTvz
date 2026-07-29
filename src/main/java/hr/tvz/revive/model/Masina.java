package hr.tvz.revive.model;

import java.io.Serializable;

public class Masina implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CIJENA_ZUPCANIKA = 3;
    public static final int BODOVI_PO_MASINI = 4;

    private String naziv;

    public Masina(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getBodovi() {
        return BODOVI_PO_MASINI;
    }
}