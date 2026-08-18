package hr.tvz.revive.reflection;

import hr.tvz.revive.model.Igrac;
import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.Masina;
import hr.tvz.revive.model.PermafrostPloca;
import hr.tvz.revive.model.PoljePermafrosta;
import hr.tvz.revive.model.Radnik;
import hr.tvz.revive.model.StanjeIgre;
import java.lang.reflect.Field;
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

        for (Class<?> klasa : MODEL_KLASE) {
            dodajAnalizuKlase(izvjestaj, klasa);
        }

        return izvjestaj.toString();
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