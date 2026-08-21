package hr.tvz.revive.model;

public enum TipRadnika {

    EXPLORER("Kopa kristale, rijetko pronađe i novu kartu", "1 kristal"),
    BUILDER("Sakuplja zupčanike iz rudnika", "2 zupčanika"),
    SCHOLAR("Prikuplja hranu iz zaliha", "2 hrane"),
    SCIENTIST("Istražuje i donosi bodove", "1 kristal");

    private final String opisSposobnosti;
    private final String uvjetAktivacije;

    TipRadnika(String opisSposobnosti, String uvjetAktivacije) {
        this.opisSposobnosti = opisSposobnosti;
        this.uvjetAktivacije = uvjetAktivacije;
    }

    public String getOpisSposobnosti() {
        return opisSposobnosti;
    }

    public String getUvjetAktivacije() {
        return uvjetAktivacije;
    }
}