package hr.tvz.revive.engine;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.Radnik;
import hr.tvz.revive.model.StanjeIgre;
import hr.tvz.revive.model.TipRadnika;
import java.util.ArrayList;
import java.util.List;

public class ReviveEngine {

    public static final int BROJ_RUNDI = 5;
    private static final int CIJENA_EXPLORER = 1;
    private static final int CIJENA_BUILDER = 2;
    private static final int CIJENA_SCHOLAR = 2;
    private static final int CIJENA_SCIENTIST = 1;

    private List<Igrac> igraci;
    private PermafrostPloca permafrostPloca;
    private List<Karta> spilKarata;
    private int trenutnaRunda;
    private int indeksIgracaNaPotezu;
    private ObradaNagrada obradaNagrada;

    public ReviveEngine() {
        this.igraci = new ArrayList<>();
        this.permafrostPloca = new PermafrostPloca();
        this.trenutnaRunda = 1;
        this.indeksIgracaNaPotezu = 0;
        this.obradaNagrada = new ObradaNagrada();
    }

    public void pokreniNovuIgru(String imePrvogIgraca, String imeDrugogIgraca) {
        igraci.clear();
        igraci.add(new Igrac(imePrvogIgraca));
        igraci.add(new Igrac(imeDrugogIgraca));
        permafrostPloca = new PermafrostPloca();

        GeneratorKarata generatorKarata = new GeneratorKarata();
        spilKarata = generatorKarata.generirajSpil();
        for (Igrac igrac : igraci) {
            igrac.getRukaKarata().addAll(generatorKarata.izvuciKarte(spilKarata, 1));
        }
        trenutnaRunda = 1;
        indeksIgracaNaPotezu = 0;
    }

    public void primijeniUcitanoStanje(StanjeIgre ucitanoStanje) {
        this.igraci = ucitanoStanje.getIgraci();
        this.permafrostPloca = ucitanoStanje.getPermafrostPloca();
        this.spilKarata = ucitanoStanje.getSpilKarata();
        this.trenutnaRunda = ucitanoStanje.getTrenutnaRunda();
        this.indeksIgracaNaPotezu = ucitanoStanje.getIndeksIgracaNaPotezu();
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

    public RezultatPoteza odigrajKartu(Karta odabranaKarta) {
        Igrac igracNaPotezu = getIgracNaPotezu();
        RezultatPoteza rezultatPoteza = new RezultatPoteza();

        if (!igracNaPotezu.getRukaKarata().contains(odabranaKarta)) {
            rezultatPoteza.setUspjesno(false);
            rezultatPoteza.setPoruka("Igrac nema tu kartu u ruci.");
            return rezultatPoteza;
        }

        switch (odabranaKarta.getTipAkcije()) {
            case DAJ_HRANU:
                igracNaPotezu.dodajHranu(odabranaKarta.getVrijednost());
                break;
            case DAJ_ZUPCANIKE:
                igracNaPotezu.dodajZupcanike(odabranaKarta.getVrijednost());
                break;
            case DAJ_KRISTAL:
                igracNaPotezu.dodajKristale(odabranaKarta.getVrijednost());
                break;
            case DAJ_BODOVE:
                igracNaPotezu.dodajBodove(odabranaKarta.getVrijednost());
                break;
            default:
                break;
        }
        igracNaPotezu.getRukaKarata().remove(odabranaKarta);

        rezultatPoteza.setUspjesno(true);
        rezultatPoteza.setPoruka("Odigrana karta: " + odabranaKarta.getNaziv() + ".");
        return rezultatPoteza;
    }

    public RezultatPoteza predajPotez() {
        RezultatPoteza rezultatPoteza = new RezultatPoteza();
        rezultatPoteza.setUspjesno(true);
        rezultatPoteza.setPoruka(getIgracNaPotezu().getImeIgraca() + " je predao potez.");
        return rezultatPoteza;
    }

    public RezultatPoteza postaviRadnika(TipRadnika tipRadnika, int redak, int stupac) {
        Igrac igracNaPotezu = getIgracNaPotezu();
        RezultatPoteza rezultatPoteza = new RezultatPoteza();

        Radnik radnik = igracNaPotezu.pronadjiNepostavljenogRadnika(tipRadnika);
        if (radnik == null) {
            rezultatPoteza.setUspjesno(false);
            rezultatPoteza.setPoruka("Vec si postavio tog radnika.");
            return rezultatPoteza;
        }

        PoljePermafrosta polje = permafrostPloca.pronadjiPolje(redak, stupac);
        if (polje == null || polje.isZauzeto()) {
            rezultatPoteza.setUspjesno(false);
            rezultatPoteza.setPoruka("To polje nije slobodno.");
            return rezultatPoteza;
        }

        if (!platiUlaznuCijenu(igracNaPotezu, tipRadnika)) {
            rezultatPoteza.setUspjesno(false);
            rezultatPoteza.setPoruka("Nedovoljno resursa za postavljanje tog radnika.");
            return rezultatPoteza;
        }

        radnik.postaviNaPolje(redak, stupac);
        polje.zauzmi(indeksIgracaNaPotezu, tipRadnika);

        rezultatPoteza.setUspjesno(true);
        rezultatPoteza.setPostavljenoPolje(polje);
        rezultatPoteza.setPoruka(tipRadnika + " je postavljen i pocinje raditi od sljedece runde.");
        return rezultatPoteza;
    }

    private boolean platiUlaznuCijenu(Igrac igrac, TipRadnika tipRadnika) {
        switch (tipRadnika) {
            case EXPLORER:
                return igrac.potrosiKristale(CIJENA_EXPLORER);
            case BUILDER:
                return igrac.potrosiZupcanike(CIJENA_BUILDER);
            case SCHOLAR:
                return igrac.potrosiHranu(CIJENA_SCHOLAR);
            case SCIENTIST:
                return igrac.potrosiKristale(CIJENA_SCIENTIST);
            default:
                return false;
        }
    }

    public List<PodatakONagradi> zavrsiPotezIPredajSljedecem() {
        indeksIgracaNaPotezu++;
        if (indeksIgracaNaPotezu >= igraci.size()) {
            indeksIgracaNaPotezu = 0;
            List<PodatakONagradi> nagrade = obradiNagradePostavljenihRadnika();
            trenutnaRunda++;
            return nagrade;
        }
        return new ArrayList<>();
    }

    private List<PodatakONagradi> obradiNagradePostavljenihRadnika() {
        List<PodatakONagradi> sveNagrade = new ArrayList<>();
        for (int i = 0; i < igraci.size(); i++) {
            Igrac igrac = igraci.get(i);
            for (Radnik radnik : igrac.getRadnici()) {
                if (radnik.isPostavljen()) {
                    String tekstNagrade = obradaNagrada.dodijeliNagraduRadnika(igrac, radnik, spilKarata);
                    sveNagrade.add(new PodatakONagradi(radnik.getRedak(), radnik.getStupac(), tekstNagrade, i));
                }
            }
        }
        return sveNagrade;
    }

    public boolean jeIgraZavrsena() {
        return trenutnaRunda > BROJ_RUNDI;
    }
}