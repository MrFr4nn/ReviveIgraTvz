package hr.tvz.revive.xml;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipRadnika;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.helpers.AttributesImpl;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class ZapisPoteza {

    private static final String PUTANJA_DATOTEKE = "revive-log.xml";

    private TransformerHandler pisacXmla;
    private OutputStream tokDatoteke;

    public void zapocniZapis() {
        try {
            tokDatoteke = new FileOutputStream(PUTANJA_DATOTEKE);
            SAXTransformerFactory tvornicaTransformera = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            pisacXmla = tvornicaTransformera.newTransformerHandler();
            pisacXmla.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");

            Result rezultat = new StreamResult(tokDatoteke);
            pisacXmla.setResult(rezultat);

            pisacXmla.startDocument();
            pisacXmla.startElement("", "", "dnevnikIgre", new AttributesImpl());

        } catch (Exception iznimka) {
            System.out.println("Greska prilikom pokretanja zapisa: " + iznimka.getMessage());
        }
    }

    public void zapisiPotez(int brojRunde, String imeIgraca, Karta odigranaKarta, TipRadnika tipRadnika) {
        if (pisacXmla == null) {
            return;
        }

        try {
            AttributesImpl atributiPoteza = new AttributesImpl();
            atributiPoteza.addAttribute("", "", "runda", "CDATA", String.valueOf(brojRunde));
            atributiPoteza.addAttribute("", "", "igrac", "CDATA", imeIgraca);
            atributiPoteza.addAttribute("", "", "karta", "CDATA", odigranaKarta.getNaziv());
            atributiPoteza.addAttribute("", "", "radnik", "CDATA", tipRadnika.name());

            pisacXmla.startElement("", "", "potez", atributiPoteza);
            pisacXmla.endElement("", "", "potez");

        } catch (Exception iznimka) {
            System.out.println("Greska prilikom zapisivanja poteza: " + iznimka.getMessage());
        }
    }

    public void zavrsiZapis() {
        if (pisacXmla == null) {
            return;
        }

        try {
            pisacXmla.endElement("", "", "dnevnikIgre");
            pisacXmla.endDocument();
            tokDatoteke.close();
        } catch (Exception iznimka) {
            System.out.println("Greska prilikom zavrsavanja zapisa: " + iznimka.getMessage());
        }
    }
}