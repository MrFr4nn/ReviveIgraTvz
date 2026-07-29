package hr.tvz.revive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Igrac implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAKSIMALNI_RESURS = 6;

    private String imeIgraca;
    private List<Karta> rukaKarata;
    private List<Radnik> radnici;
    private List<Masina> izgradjeneMasine;
    private int hrana;
    private int zupcanici;
    private int bodovi;

    public Igrac(String imeIgraca) {
        this.imeIgraca = imeIgraca;
        this.rukaKarata = new ArrayList<>();
        this.radnici = new ArrayList<>();
        this.izgradjeneMasine = new ArrayList<>();
        this.hrana = 2;
        this.zupcanici = 2;
        this.bodovi = 0;

        radnici.add(new Radnik(TipRadnika.EXPLORER));
        radnici.add(new Radnik(TipRadnika.BUILDER));
        radnici.add(new Radnik(TipRadnika.SCHOLAR));
        radnici.add(new Radnik(TipRadnika.SCIENTIST));
    }

    public String getImeIgraca() {
        return imeIgraca;
    }

    public List<Karta> getRukaKarata() {
        return rukaKarata;
    }

    public List<Radnik> getRadnici() {
        return radnici;
    }

    public List<Masina> getIzgradjeneMasine() {
        return izgradjeneMasine;
    }

    public int getHrana() {
        return hrana;
    }

    public int getZupcanici() {
        return zupcanici;
    }

    public int getBodovi() {
        return bodovi;
    }

    public void dodajHranu(int kolicina) {
        hrana = Math.min(hrana + kolicina, MAKSIMALNI_RESURS);
    }

    public void dodajZupcanike(int kolicina) {
        zupcanici = Math.min(zupcanici + kolicina, MAKSIMALNI_RESURS);
    }

    public void dodajBodove(int kolicina) {
        bodovi = bodovi + kolicina;
    }

    public boolean potrosiZupcanike(int kolicina) {
        if (zupcanici < kolicina) {
            return false;
        }
        zupcanici = zupcanici - kolicina;
        return true;
    }

    public Radnik pronadjiSlobodnogRadnika(TipRadnika tip) {
        for (Radnik radnik : radnici) {
            if (radnik.getTip() == tip && !radnik.isPostavljen()) {
                return radnik;
            }
        }
        return null;
    }

    public void resetirajRadnike() {
        for (Radnik radnik : radnici) {
            radnik.resetiraj();
        }
    }

    public int izracunajUkupneBodoveNaKraju() {
        int ukupno = bodovi;
        for (Masina masina : izgradjeneMasine) {
            ukupno = ukupno + masina.getBodovi();
        }
        return ukupno;
    }
}