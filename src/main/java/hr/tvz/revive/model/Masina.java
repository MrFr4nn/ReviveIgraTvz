package hr.tvz.revive.model;

import java.io.Serializable;

/**
 * Predstavlja jednu izgradjenu Masinu (postignuce Buildera).
 * Svaka izgradjena Masina na kraju igre donosi fiksni broj bodova.
 */
public class Masina implements Serializable {

    private static final long serialVersionUID = 1L;

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