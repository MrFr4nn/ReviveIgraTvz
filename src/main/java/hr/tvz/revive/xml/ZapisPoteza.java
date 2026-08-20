package hr.tvz.revive.xml;

import hr.tvz.revive.model.Karta;
import hr.tvz.revive.model.TipRadnika;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZapisPoteza {

    private static final String PUTANJA_DATOTEKE = "revive-log.xml";

    private List<PodatakOPotezu> odigraniPotezi;

    public void zapocniZapis() {
        odigraniPotezi = new ArrayList<>();
        zapisiNaDisk();
    }

    public void zapisiPostavljanjeRadnika(int brojRunde, String imeIgraca, TipRadnika tipRadnika) {
        if (odigraniPotezi == null) {
            return;
        }
        odigraniPotezi.add(new PodatakOPotezu(brojRunde, imeIgraca, "postavljanje radnika", tipRadnika.name()));
        zapisiNaDisk();
    }

    public void zapisiPotezKarte(int brojRunde, String imeIgraca, Karta odigranaKarta) {
        if (odigraniPotezi == null) {
            return;
        }
        odigraniPotezi.add(new PodatakOPotezu(brojRunde, imeIgraca, odigranaKarta.getNaziv(), "KARTA"));
        zapisiNaDisk();
    }

    public void zapisiPredajuPoteza(int brojRunde, String imeIgraca) {
        if (odigraniPotezi == null) {
            return;
        }
        odigraniPotezi.add(new PodatakOPotezu(brojRunde, imeIgraca, "-", "PREDAJA"));
        zapisiNaDisk();
    }

    public void zavrsiZapis() {
        zapisiNaDisk();
    }

    private void zapisiNaDisk() {
        try {
            DocumentBuilderFactory tvornicaGraditelja = DocumentBuilderFactory.newInstance();
            DocumentBuilder graditeljDokumenta = tvornicaGraditelja.newDocumentBuilder();
            Document dokument = graditeljDokumenta.newDocument();

            Element korijenskiElement = dokument.createElement("dnevnikIgre");
            dokument.appendChild(korijenskiElement);

            if (odigraniPotezi != null) {
                for (PodatakOPotezu podatak : odigraniPotezi) {
                    korijenskiElement.appendChild(izgradiElementPoteza(dokument, podatak));
                }
            }

            zapisiDokumentUFile(dokument);

        } catch (Exception iznimka) {
            System.out.println("Greska prilikom zapisivanja dnevnika: " + iznimka.getMessage());
        }
    }

    private Element izgradiElementPoteza(Document dokument, PodatakOPotezu podatak) {
        Element elementPoteza = dokument.createElement("potez");
        elementPoteza.setAttribute("runda", String.valueOf(podatak.getBrojRunde()));
        elementPoteza.setAttribute("igrac", podatak.getImeIgraca());
        elementPoteza.setAttribute("karta", podatak.getNazivKarte());
        elementPoteza.setAttribute("radnik", podatak.getTipRadnika());
        return elementPoteza;
    }

    private void zapisiDokumentUFile(Document dokument) throws Exception {
        TransformerFactory tvornicaTransformera = TransformerFactory.newInstance();
        Transformer transformer = tvornicaTransformera.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(dokument), new StreamResult(new File(PUTANJA_DATOTEKE)));
    }
}