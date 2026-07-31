package hr.tvz.revive.model;

import java.io.Serializable;

public class Karta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String naziv;
    private TipAkcijeKarte tipAkcije;
    private int vrijednost;

    public Karta(String naziv, TipAkcijeKarte tipAkcije, int vrijednost) {
        this.naziv = naziv;
        this.tipAkcije = tipAkcije;
        this.vrijednost = vrijednost;
    }

    public String getNaziv() {
        return naziv;
    }

    public TipAkcijeKarte getTipAkcije() {
        return tipAkcije;
    }

    public int getVrijednost() {
        return vrijednost;
    }

    @Override
    public String toString() {
        return naziv + " (" + tipAkcije + " +" + vrijednost + ")";
    }
}