package hr.tvz.revive.engine;

import hr.tvz.revive.model.PoljePermafrosta;

public class RezultatPoteza {

    private boolean uspjesno;
    private String poruka;
    private PoljePermafrosta otopljenoPolje;

    public boolean isUspjesno() {
        return uspjesno;
    }

    public void setUspjesno(boolean uspjesno) {
        this.uspjesno = uspjesno;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public PoljePermafrosta getOtopljenoPolje() {
        return otopljenoPolje;
    }

    public void setOtopljenoPolje(PoljePermafrosta otopljenoPolje) {
        this.otopljenoPolje = otopljenoPolje;
    }
}