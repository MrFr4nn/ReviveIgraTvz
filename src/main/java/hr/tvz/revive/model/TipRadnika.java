package hr.tvz.revive.model;

public enum TipRadnika {

    EXPLORER("Topi jedno polje na Permafrost ploci i otkriva bonus"),
    BUILDER("Gradi jednu Masinu ako igrac ima dovoljno zupcanika"),
    SCHOLAR("Izvlaci jednu novu kartu iz spila u ruku igraca"),
    SCIENTIST("Odigrava kartu iz ruke i pretvara je u bodove");

    private final String opisSposobnosti;

    TipRadnika(String opisSposobnosti) {
        this.opisSposobnosti = opisSposobnosti;
    }

    public String getOpisSposobnosti() {
        return opisSposobnosti;
    }
}