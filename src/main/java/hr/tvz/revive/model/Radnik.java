package hr.tvz.revive.model;
import java.io.Serializable;

public class Radnik implements Serializable {

    private static final long serialVersionUID = 1L;

    private TipRadnika tip;
    private boolean postavljen;

    public Radnik(TipRadnika tip) {
        this.tip = tip;
        this.postavljen = false;
    }

    public TipRadnika getTip() {
        return tip;
    }

    public boolean isPostavljen() {
        return postavljen;
    }

    public void postavi() {
        this.postavljen = true;
    }

    public void resetiraj() {
        this.postavljen = false;
    }
}