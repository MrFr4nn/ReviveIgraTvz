package hr.tvz.revive.reflection;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.Masina;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.Radnik;
import hr.tvz.revive.model.StanjeIgre;
import hr.tvz.revive.model.TipRadnika;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class KatalogRadnikaGenerator {

    private static final Class<?>[] MODEL_KLASE = {
            Igrac.class, Karta.class, Radnik.class, Masina.class,
            PermafrostPloca.class, PoljePermafrosta.class, StanjeIgre.class
    };

    public String generirajKatalog() {
        StringBuilder izvjestaj = new StringBuilder();
        izvjestaj.append("=== REFLECTION ANALIZA CIJELOG MODELA IGRE ===\n\n");

        dodajKatalogTipovaRadnika(izvjestaj);

        for (Class<?> klasa : MODEL_KLASE) {
            dodajAnalizuKlase(izvjestaj, klasa);
        }

        return izvjestaj.toString();
    }

    private void dodajKatalogTipovaRadnika(StringBuilder izvjestaj) {
        izvjestaj.append("=== KATALOG TIPOVA RADNIKA (sposobnosti i uvjeti aktivacije) ===\n\n");

        Object[] sveVrijednostiEnuma = TipRadnika.class.getEnumConstants();
        Method getterOpisa = dohvatiMetodu("getOpisSposobnosti");
        Method getterUvjeta = dohvatiMetodu("getUvjetAktivacije");

        for (Object vrijednostEnuma : sveVrijednostiEnuma) {
            String naziv = ((Enum<?>) vrijednostEnuma).name();
            String opis = pozoviGetter(getterOpisa, vrijednostEnuma);
            String uvjet = pozoviGetter(getterUvjeta, vrijednostEnuma);

            izvjestaj.append(naziv).append(":\n");
            izvjestaj.append("  Sposobnost: ").append(opis).append("\n");
            izvjestaj.append("  Uvjet aktivacije Permafrost polja: ").append(uvjet).append("\n\n");
        }
    }

    private Method dohvatiMetodu(String nazivMetode) {
        try {
            return TipRadnika.class.getMethod(nazivMetode);
        } catch (NoSuchMethodException iznimka) {
            return null;
        }
    }

    private String pozoviGetter(Method getter, Object naCemu) {
        if (getter == null) {
            return "nepoznato";
        }
        try {
            return String.valueOf(getter.invoke(naCemu));
        } catch (IllegalAccessException | InvocationTargetException iznimka) {
            return "nepoznato";
        }
    }

    private void dodajAnalizuKlase(StringBuilder izvjestaj, Class<?> klasa) {
        izvjestaj.append("--- KLASA: ").append(klasa.getSimpleName()).append(" ---\n");

        Field[] svaPolja = klasa.getDeclaredFields();
        izvjestaj.append("Atributi (").append(svaPolja.length).append("):\n");
        for (Field polje : svaPolja) {
            String modifikator = Modifier.toString(polje.getModifiers());
            izvjestaj.append("  ").append(modifikator).append(" ")
                    .append(polje.getType().getSimpleName()).append(" ")
                    .append(polje.getName()).append("\n");
        }

        Method[] sveMetode = klasa.getDeclaredMethods();
        izvjestaj.append("Metode (").append(sveMetode.length).append("):\n");
        for (Method metoda : sveMetode) {
            String modifikator = Modifier.toString(metoda.getModifiers());
            izvjestaj.append("  ").append(modifikator).append(" ")
                    .append(metoda.getReturnType().getSimpleName()).append(" ")
                    .append(metoda.getName()).append("(").append(opisiParametre(metoda)).append(")\n");
        }
        izvjestaj.append("\n");
    }

    private String opisiParametre(Method metoda) {
        Class<?>[] tipoviParametara = metoda.getParameterTypes();
        StringBuilder opisParametara = new StringBuilder();

        for (int i = 0; i < tipoviParametara.length; i++) {
            if (i > 0) {
                opisParametara.append(", ");
            }
            opisParametara.append(tipoviParametara[i].getSimpleName());
        }
        return opisParametara.toString();
    }
}