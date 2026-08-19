package hr.tvz.revive.model;

import java.io.Serializable;

public class Radnik implements Serializable {

    private static final long serialVersionUID = 1L;

    private TipRadnika tip;
    private boolean postavljen;
    private int redak;
    private int stupac;

    public Radnik(TipRadnika tip) {
        this.tip = tip;
        this.postavljen = false;
        this.redak = -1;
        this.stupac = -1;
    }

    public TipRadnika getTip() {
        return tip;
    }

    public boolean isPostavljen() {
        return postavljen;
    }

    public int getRedak() {
        return redak;
    }

    public int getStupac() {
        return stupac;
    }

    public void postaviNaPolje(int redak, int stupac) {
        this.postavljen = true;
        this.redak = redak;
        this.stupac = stupac;
    }
}