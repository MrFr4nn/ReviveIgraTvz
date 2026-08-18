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
    private int kristali;
    private int bodovi;
    private int brojNaucenihZapisa;
    private int brojIzucenihEksperimenata;

    public Igrac(String imeIgraca) {
        this.imeIgraca = imeIgraca;
        this.rukaKarata = new ArrayList<>();
        this.radnici = new ArrayList<>();
        this.izgradjeneMasine = new ArrayList<>();
        this.hrana = 2;
        this.zupcanici = 2;
        this.kristali = 0;
        this.bodovi = 0;
        this.brojNaucenihZapisa = 0;
        this.brojIzucenihEksperimenata = 0;

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

    public int getKristali() {
        return kristali;
    }

    public int getBodovi() {
        return bodovi;
    }

    public int getBrojNaucenihZapisa() {
        return brojNaucenihZapisa;
    }

    public int getBrojIzucenihEksperimenata() {
        return brojIzucenihEksperimenata;
    }

    public void dodajHranu(int kolicina) {
        hrana = Math.min(hrana + kolicina, MAKSIMALNI_RESURS);
    }

    public void dodajZupcanike(int kolicina) {
        zupcanici = Math.min(zupcanici + kolicina, MAKSIMALNI_RESURS);
    }

    public void dodajKristale(int kolicina) {
        kristali = Math.min(kristali + kolicina, MAKSIMALNI_RESURS);
    }

    public void dodajBodove(int kolicina) {
        bodovi = bodovi + kolicina;
    }

    public void dodajNaucenZapis() {
        brojNaucenihZapisa = brojNaucenihZapisa + 1;
    }

    public void dodajIzucenEksperiment() {
        brojIzucenihEksperimenata = brojIzucenihEksperimenata + 1;
    }

    public boolean potrosiZupcanike(int kolicina) {
        if (zupcanici < kolicina) {
            return false;
        }
        zupcanici = zupcanici - kolicina;
        return true;
    }

    public boolean potrosiHranu(int kolicina) {
        if (hrana < kolicina) {
            return false;
        }
        hrana = hrana - kolicina;
        return true;
    }

    public boolean potrosiKristale(int kolicina) {
        if (kristali < kolicina) {
            return false;
        }
        kristali = kristali - kolicina;
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