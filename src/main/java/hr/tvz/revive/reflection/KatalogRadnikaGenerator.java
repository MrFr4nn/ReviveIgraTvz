package hr.tvz.revive.reflection;

import hr.tvz.revive.model.Igrac;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class KatalogRadnikaGenerator {

    public String generirajKatalog() {
        StringBuilder izvjestaj = new StringBuilder();
        Class<Igrac> klasaIgrac = Igrac.class;

        izvjestaj.append("=== REFLECTION ANALIZA IGRE: ").append(klasaIgrac.getSimpleName()).append(" ===\n\n");
        dodajPolja(izvjestaj, klasaIgrac);
        dodajMetode(izvjestaj, klasaIgrac);

        return izvjestaj.toString();
    }

    private void dodajPolja(StringBuilder izvjestaj, Class<Igrac> klasaIgrac) {
        izvjestaj.append("--- ATRIBUTI (polja) klase, otkriveni preko getDeclaredFields() ---\n");
        Field[] svaPolja = klasaIgrac.getDeclaredFields();

        for (Field polje : svaPolja) {
            String modifikator = Modifier.toString(polje.getModifiers());
            String tipPolja = polje.getType().getSimpleName();
            izvjestaj.append(String.format("  %s %s %s%n", modifikator, tipPolja, polje.getName()));
        }
        izvjestaj.append("\nUkupno atributa: ").append(svaPolja.length).append("\n\n");
    }

    private void dodajMetode(StringBuilder izvjestaj, Class<Igrac> klasaIgrac) {
        izvjestaj.append("--- METODE klase, otkrivene preko getDeclaredMethods() ---\n");
        Method[] sveMetode = klasaIgrac.getDeclaredMethods();

        for (Method metoda : sveMetode) {
            String modifikator = Modifier.toString(metoda.getModifiers());
            String povratniTip = metoda.getReturnType().getSimpleName();
            String parametri = opisiParametre(metoda);
            izvjestaj.append(String.format("  %s %s %s(%s)%n", modifikator, povratniTip, metoda.getName(), parametri));
        }
        izvjestaj.append("\nUkupno metoda: ").append(sveMetode.length).append("\n\n");
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