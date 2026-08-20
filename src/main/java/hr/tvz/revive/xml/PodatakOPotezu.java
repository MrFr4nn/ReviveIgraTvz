package hr.tvz.revive.xml;

public class PodatakOPotezu {

    private int brojRunde;
    private String imeIgraca;
    private String nazivKarte;
    private String tipRadnika;

    public PodatakOPotezu(int brojRunde, String imeIgraca, String nazivKarte, String tipRadnika) {
        this.brojRunde = brojRunde;
        this.imeIgraca = imeIgraca;
        this.nazivKarte = nazivKarte;
        this.tipRadnika = tipRadnika;
    }

    public int getBrojRunde() {
        return brojRunde;
    }

    public String getImeIgraca() {
        return imeIgraca;
    }

    public String getNazivKarte() {
        return nazivKarte;
    }

    public String getTipRadnika() {
        return tipRadnika;
    }

    @Override
    public String toString() {
        if ("KARTA".equals(tipRadnika)) {
            return "Runda " + brojRunde + ": " + imeIgraca + " je odigrao kartu '" + nazivKarte + "'";
        }
        if ("PREDAJA".equals(tipRadnika)) {
            return "Runda " + brojRunde + ": " + imeIgraca + " je predao potez";
        }
        if ("NAGRADA".equals(tipRadnika)) {
            return "Runda " + brojRunde + ": " + imeIgraca + " je dobio nagradu (" + nazivKarte + ")";
        }
        return "Runda " + brojRunde + ": " + imeIgraca + " je postavio radnika " + tipRadnika;
    }
}