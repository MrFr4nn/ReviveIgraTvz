package hr.tvz.revive.model;

public enum TipAkcijeKarte {

    DAJ_HRANU,
    DAJ_ZUPCANIKE,
    DAJ_BODOVE,
    DAJ_KRISTAL;

    public TipRadnika getPovezaniTipRadnika() {
        switch (this) {
            case DAJ_ZUPCANIKE:
                return TipRadnika.BUILDER;
            case DAJ_BODOVE:
                return TipRadnika.SCIENTIST;
            case DAJ_HRANU:
                return TipRadnika.SCHOLAR;
            case DAJ_KRISTAL:
                return TipRadnika.EXPLORER;
            default:
                return null;
        }
    }
}