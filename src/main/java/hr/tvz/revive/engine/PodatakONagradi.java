package hr.tvz.revive.engine;

public class PodatakONagradi {

    private int redak;
    private int stupac;
    private String tekstNagrade;
    private int indeksVlasnika;

    public PodatakONagradi(int redak, int stupac, String tekstNagrade, int indeksVlasnika) {
        this.redak = redak;
        this.stupac = stupac;
        this.tekstNagrade = tekstNagrade;
        this.indeksVlasnika = indeksVlasnika;
    }

    public int getRedak() {
        return redak;
    }

    public int getStupac() {
        return stupac;
    }

    public String getTekstNagrade() {
        return tekstNagrade;
    }

    public int getIndeksVlasnika() {
        return indeksVlasnika;
    }
}