package hr.tvz.revive.model;

import java.io.Serializable;

public class PoljePermafrosta implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int KOLICINA_HRANE_NAGRADA = 2;
    public static final int KOLICINA_ZUPCANIKA_NAGRADA = 2;
    public static final int KOLICINA_BODOVA_NAGRADA = 3;

    private int redak;
    private int stupac;
    private boolean otopljeno;
    private VrstaNagradePermafrosta vrstaNagrade;

    public PoljePermafrosta(int redak, int stupac, VrstaNagradePermafrosta vrstaNagrade) {
        this.redak = redak;
        this.stupac = stupac;
        this.vrstaNagrade = vrstaNagrade;
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

    public VrstaNagradePermafrosta getVrstaNagrade() {
        return vrstaNagrade;
    }

    public void otopiIPrimijeniNagradu(Igrac igrac) {
        if (otopljeno) {
            return;
        }
        otopljeno = true;

        switch (vrstaNagrade) {
            case HRANA:
                igrac.dodajHranu(KOLICINA_HRANE_NAGRADA);
                break;
            case ZUPCANICI:
                igrac.dodajZupcanike(KOLICINA_ZUPCANIKA_NAGRADA);
                break;
            case BODOVI:
                igrac.dodajBodove(KOLICINA_BODOVA_NAGRADA);
                break;
            case PRAZNO:
                break;
            default:
                break;
        }
    }
}