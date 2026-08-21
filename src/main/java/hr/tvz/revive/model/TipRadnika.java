package hr.tvz.revive.model;

public enum TipRadnika {

    EXPLORER("Kopa kristale, rijetko pronađe i novu kartu"),
    BUILDER("Sakuplja zupčanike iz rudnika"),
    SCHOLAR("Prikuplja hranu iz zaliha"),
    SCIENTIST("Istražuje i donosi bodove");

    private final String opisSposobnosti;

    TipRadnika(String opisSposobnosti) {
        this.opisSposobnosti = opisSposobnosti;
    }

    public String getOpisSposobnosti() {
        return opisSposobnosti;
    }
}