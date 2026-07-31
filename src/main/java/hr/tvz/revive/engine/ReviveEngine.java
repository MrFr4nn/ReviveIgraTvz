package hr.tvz.revive.engine;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.Masina;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.Radnik;
import hr.tvz.revive.model.TipRadnika;
import hr.tvz.revive.model.VrstaNagradePermafrosta;
import java.util.ArrayList;
import java.util.List;

public class ReviveEngine {

    public static final int BROJ_RUNDI = 5;
    public static final int MAKSIMALNI_BROJ_IGRACA = 2;

    private List<Igrac> igraci;
    private PermafrostPloca permafrostPloca;
    private List<Karta> spilKarata;
    private int trenutnaRunda;
    private int indeksIgracaNaPotezu;

    public ReviveEngine() {
        this.igraci = new ArrayList<>();
        this.permafrostPloca = new PermafrostPloca();
        this.trenutnaRunda = 1;
        this.indeksIgracaNaPotezu = 0;
    }
    public void pokreniNovuIgru(String imePrvogIgraca, String imeDrugogIgraca) {
        igraci.clear();
        igraci.add(new Igrac(imePrvogIgraca));
        igraci.add(new Igrac(imeDrugogIgraca));

        GeneratorKarata generatorKarata = new GeneratorKarata();
        spilKarata = generatorKarata.generirajSpil();

        for (Igrac igrac : igraci) {
            igrac.getRukaKarata().addAll(generatorKarata.podijeliPocetnuRuku(spilKarata, 3));
        }

        trenutnaRunda = 1;
        indeksIgracaNaPotezu = 0;
    }

    public Igrac getIgracNaPotezu() {
        return igraci.get(indeksIgracaNaPotezu);
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

    public RezultatPoteza izvrsiPotez(Karta odabranaKarta, TipRadnika tipRadnika) {
        return izvrsiPotez(odabranaKarta, tipRadnika, -1, -1);
    }

    public RezultatPoteza izvrsiPotez(Karta odabranaKarta, TipRadnika tipRadnika, int redakPolja, int stupacPolja) {
        Igrac igracNaPotezu = getIgracNaPotezu();
        RezultatPoteza rezultatPoteza = new RezultatPoteza();
        String greskaValidacije = validirajPotez(igracNaPotezu, odabranaKarta, tipRadnika);

        if (greskaValidacije != null) {
            rezultatPoteza.setUspjesno(false);
            rezultatPoteza.setPoruka(greskaValidacije);
            return rezultatPoteza;
        }

        Radnik slobodniRadnik = igracNaPotezu.pronadjiSlobodnogRadnika(tipRadnika);
        primijeniEfektKarte(igracNaPotezu, odabranaKarta);
        igracNaPotezu.getRukaKarata().remove(odabranaKarta);
        primijeniEfektRadnika(igracNaPotezu, slobodniRadnik, rezultatPoteza, redakPolja, stupacPolja);

        rezultatPoteza.setUspjesno(true);
        rezultatPoteza.setPoruka("Potez uspjesno odigran.");
        return rezultatPoteza;
    }

    private String validirajPotez(Igrac igracNaPotezu, Karta odabranaKarta, TipRadnika tipRadnika) {
        if (!igracNaPotezu.getRukaKarata().contains(odabranaKarta)) {
            return "Igrac nema tu kartu u ruci.";
        }
        if (odabranaKarta.getTipAkcije().getPovezaniTipRadnika() != tipRadnika) {
            return "Ta karta ne odgovara odabranom tipu radnika.";
        }
        if (igracNaPotezu.pronadjiSlobodnogRadnika(tipRadnika) == null) {
            return "Nema slobodnog radnika tog tipa.";
        }
        return null;
    }

    private void primijeniEfektKarte(Igrac igrac, Karta karta) {
        switch (karta.getTipAkcije()) {
            case DAJ_HRANU:
                igrac.dodajHranu(karta.getVrijednost());
                break;
            case DAJ_ZUPCANIKE:
                igrac.dodajZupcanike(karta.getVrijednost());
                break;
            case DAJ_BODOVE:
                igrac.dodajBodove(karta.getVrijednost());
                break;
            case DAJ_KRISTAL:
                igrac.dodajZupcanike(karta.getVrijednost());
                igrac.dodajHranu(karta.getVrijednost());
                break;
            default:
                break;
        }
    }

    private void primijeniEfektRadnika(Igrac igrac, Radnik radnik, RezultatPoteza rezultatPoteza,
                                       int redakPolja, int stupacPolja) {
        radnik.postavi();
        if (radnik.getTip() == TipRadnika.EXPLORER) {
            izvrsiAkcijuExplorer(igrac, rezultatPoteza, redakPolja, stupacPolja);
        } else if (radnik.getTip() == TipRadnika.BUILDER) {
            izvrsiAkcijuBuilder(igrac, rezultatPoteza);
        } else if (radnik.getTip() == TipRadnika.SCHOLAR) {
            izvrsiAkcijuScholar(igrac, rezultatPoteza);
        } else if (radnik.getTip() == TipRadnika.SCIENTIST) {
            izvrsiAkcijuScientist(igrac, rezultatPoteza);
        }
    }

    private void izvrsiAkcijuExplorer(Igrac igrac, RezultatPoteza rezultatPoteza, int redakPolja, int stupacPolja) {
        PoljePermafrosta poljeZaTopljenje = permafrostPloca.pronadjiPoljeZaOtapanje(redakPolja, stupacPolja);

        if (poljeZaTopljenje == null) {
            rezultatPoteza.setPoruka("Odabrano polje ne postoji ili je vec otopljeno.");
            return;
        }

        VrstaNagradePermafrosta vrstaNagrade = poljeZaTopljenje.getVrstaNagrade();
        poljeZaTopljenje.otopiIPrimijeniNagradu(igrac);
        rezultatPoteza.setOtopljenoPolje(poljeZaTopljenje);
        rezultatPoteza.setPoruka("Explorer je otopio polje i osvojio nagradu: " + vrstaNagrade + ".");
    }

    private void izvrsiAkcijuBuilder(Igrac igrac, RezultatPoteza rezultatPoteza) {
        boolean placanjeUspjesno = igrac.potrosiZupcanike(Masina.CIJENA_ZUPCANIKA);
        if (placanjeUspjesno) {
            Masina novaMasina = new Masina("Masina broj " + (igrac.getIzgradjeneMasine().size() + 1));
            igrac.getIzgradjeneMasine().add(novaMasina);
            rezultatPoteza.setPoruka("Builder je izgradio novu Masinu.");
        } else {
            rezultatPoteza.setPoruka("Nedovoljno zupcanika za gradnju Masine.");
        }
    }

    private void izvrsiAkcijuScholar(Igrac igrac, RezultatPoteza rezultatPoteza) {
        if (!spilKarata.isEmpty()) {
            Karta izvucenaKarta = spilKarata.remove(0);
            igrac.getRukaKarata().add(izvucenaKarta);
            rezultatPoteza.setPoruka("Scholar je izvukao kartu: " + izvucenaKarta.getNaziv());
        } else {
            rezultatPoteza.setPoruka("Spil karata je prazan.");
        }
    }

    private void izvrsiAkcijuScientist(Igrac igrac, RezultatPoteza rezultatPoteza) {
        igrac.dodajBodove(1);
        rezultatPoteza.setPoruka("Scientist je pretvorio znanje u 1 dodatni bod.");
    }

    public void zavrsiPotezIPredajSljedecem() {
        indeksIgracaNaPotezu++;
        if (indeksIgracaNaPotezu >= igraci.size()) {
            indeksIgracaNaPotezu = 0;
            trenutnaRunda++;
            for (Igrac igrac : igraci) {
                igrac.resetirajRadnike();
            }
        }
    }

    public boolean jeIgraZavrsena() {
        return trenutnaRunda > BROJ_RUNDI;
    }
}