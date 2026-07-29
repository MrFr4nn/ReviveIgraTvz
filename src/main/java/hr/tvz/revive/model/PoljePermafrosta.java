package hr.tvz.revive.model;

import java.io.Serializable;

public class PoljePermafrosta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int redak;
    private int stupac;
    private boolean otopljeno;
    private int skriveniBonusBodova;

    public PoljePermafrosta(int redak, int stupac, int skriveniBonusBodova) {
        this.redak = redak;
        this.stupac = stupac;
        this.skriveniBonusBodova = skriveniBonusBodova;
        this.otopljeno = false;
    }

    public int getRedak() {
        return redak;
    }

    public int getStupac() {
        return stupac;
    }

    public boolean isOtopljeno() {
        return otopljeno;
    }

    public int getSkriveniBonusBodova() {
        return skriveniBonusBodova;
    }

    public int otopiIVratiBonus() {
        if (otopljeno) {
            return 0;
        }
        otopljeno = true;
        return skriveniBonusBodova;
    }
}