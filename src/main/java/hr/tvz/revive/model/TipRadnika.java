package hr.tvz.revive.model;

public enum TipRadnika {

    EXPLORER("Kopa kristale, rijetko pronadje i novu kartu"),
    BUILDER("Sakuplja zupcanike iz rudnika"),
    SCHOLAR("Prikuplja hranu iz zaliha"),
    SCIENTIST("Istrazuje i donosi bodove");

    private final String opisSposobnosti;

    TipRadnika(String opisSposobnosti) {
        this.opisSposobnosti = opisSposobnosti;
    }

    public String getOpisSposobnosti() {
        return opisSposobnosti;
    }
}