package hr.tvz.revive.reflection;

import hr.tvz.revive.model.TipRadnika;
import java.lang.reflect.Field;

public class KatalogRadnikaGenerator {

    public String generirajKatalog() {
        StringBuilder katalogTeksta = new StringBuilder();
        katalogTeksta.append("=== KATALOG TIPOVA RADNIKA ===\n\n");

        try {
            Class<TipRadnika> klasaTipRadnika = TipRadnika.class;
            TipRadnika[] sveVrijednosti = klasaTipRadnika.getEnumConstants();

            for (TipRadnika vrijednost : sveVrijednosti) {
                katalogTeksta.append(procitajPodatkeOJednomRadniku(klasaTipRadnika, vrijednost));
            }
        } catch (IllegalAccessException | NoSuchFieldException iznimka) {
            katalogTeksta.append("Greska prilikom citanja kataloga: ").append(iznimka.getMessage());
        }

        return katalogTeksta.toString();
    }

    private String procitajPodatkeOJednomRadniku(Class<TipRadnika> klasaTipRadnika, TipRadnika vrijednost)
            throws IllegalAccessException, NoSuchFieldException {

        StringBuilder redakKataloga = new StringBuilder();

        Field poljeImena = klasaTipRadnika.getField("name");
        String imeTipa = vrijednost.name();

        Field poljeOpisa = klasaTipRadnika.getDeclaredField("opisSposobnosti");
        poljeOpisa.setAccessible(true);
        String opisSposobnosti = (String) poljeOpisa.get(vrijednost);

        redakKataloga.append("- ").append(imeTipa).append(": ").append(opisSposobnosti).append("\n");

        return redakKataloga.toString();
    }
}