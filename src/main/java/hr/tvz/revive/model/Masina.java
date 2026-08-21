package hr.tvz.revive.model;

import java.io.Serializable;

public class Masina implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int BODOVI_PO_MASINI = 4;

    public int getBodovi() {
        return BODOVI_PO_MASINI;
    }
}