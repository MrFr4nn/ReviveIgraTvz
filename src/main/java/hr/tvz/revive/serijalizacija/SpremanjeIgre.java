package hr.tvz.revive.serijalizacija;

import hr.tvz.revive.model.StanjeIgre;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SpremanjeIgre {

    private static final String PUTANJA_DATOTEKE = "spremljena-igra.ser";

    public boolean spremiIgru(StanjeIgre stanjeIgre) {
        try (FileOutputStream tokDatoteke = new FileOutputStream(PUTANJA_DATOTEKE);
             ObjectOutputStream tokObjekata = new ObjectOutputStream(tokDatoteke)) {

            tokObjekata.writeObject(stanjeIgre);
            return true;

        } catch (IOException iznimka) {
            System.out.println("Greška prilikom spremanja igre: " + iznimka.getMessage());
            return false;
        }
    }

    public StanjeIgre ucitajIgru() {
        try (FileInputStream tokDatoteke = new FileInputStream(PUTANJA_DATOTEKE);
             ObjectInputStream tokObjekata = new ObjectInputStream(tokDatoteke)) {

            return (StanjeIgre) tokObjekata.readObject();

        } catch (IOException | ClassNotFoundException iznimka) {
            System.out.println("Greška prilikom učitavanja igre: " + iznimka.getMessage());
            return null;
        }
    }

    public void obrisiSpremljenuIgru() {
        File datoteka = new File(PUTANJA_DATOTEKE);
        if (datoteka.exists() && !datoteka.delete()) {
            System.out.println("Greška prilikom brisanja stare spremljene igre.");
        }
    }
}