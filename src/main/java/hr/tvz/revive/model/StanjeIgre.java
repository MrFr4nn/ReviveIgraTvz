package hr.tvz.revive.model;

import java.io.Serializable;
import java.util.List;

public class StanjeIgre implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Igrac> igraci;
    private PermafrostPloca permafrostPloca;
    private List<Karta> spilKarata;
    private int trenutnaRunda;
    private int indeksIgracaNaPotezu;

    public StanjeIgre(List<Igrac> igraci, PermafrostPloca permafrostPloca,
                      List<Karta> spilKarata, int trenutnaRunda, int indeksIgracaNaPotezu) {
        this.igraci = igraci;
        this.permafrostPloca = permafrostPloca;
        this.spilKarata = spilKarata;
        this.trenutnaRunda = trenutnaRunda;
        this.indeksIgracaNaPotezu = indeksIgracaNaPotezu;
    }

    public List<Igrac> getIgraci() {
        return igraci;
    }

    public PermafrostPloca getPermafrostPloca() {
        return permafrostPloca;
    }

    public List<Karta> getSpilKarata() {
        return spilKarata;
    }

    public int getTrenutnaRunda() {
        return trenutnaRunda;
    }

    public int getIndeksIgracaNaPotezu() {
        return indeksIgracaNaPotezu;
    }
}