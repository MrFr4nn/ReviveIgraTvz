package hr.tvz.revive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.SAXException;

public class ReplaySustav {

    public Map<Integer, List<PodatakOPotezu>> ucitajIGrupirajPoRundama(String putanjaDatoteke) {
        Map<Integer, List<PodatakOPotezu>> potPoRundama = new TreeMap<>();

        try {
            DocumentBuilderFactory tvornicaGraditelja = DocumentBuilderFactory.newInstance();
            DocumentBuilder graditeljDokumenta = tvornicaGraditelja.newDocumentBuilder();
            Document dokument = graditeljDokumenta.parse(new File(putanjaDatoteke));

            dokument.getDocumentElement().normalize();
            NodeList svilPotezi = dokument.getElementsByTagName("potez");

            for (int i = 0; i < svilPotezi.getLength(); i++) {
                Element elementPoteza = (Element) svilPotezi.item(i);
                PodatakOPotezu podatak = pretvoriUPodatak(elementPoteza);

                potPoRundama.computeIfAbsent(podatak.getBrojRunde(), k -> new ArrayList<>()).add(podatak);
            }

        } catch (ParserConfigurationException | SAXException | IOException iznimka) {
            System.out.println("Greska prilikom ucitavanja replaya: " + iznimka.getMessage());
        }

        return potPoRundama;
    }

    private PodatakOPotezu pretvoriUPodatak(Element elementPoteza) {
        int brojRunde = Integer.parseInt(elementPoteza.getAttribute("runda"));
        String imeIgraca = elementPoteza.getAttribute("igrac");
        String nazivKarte = elementPoteza.getAttribute("karta");
        String tipRadnika = elementPoteza.getAttribute("radnik");

        return new PodatakOPotezu(brojRunde, imeIgraca, nazivKarte, tipRadnika);
    }

    public String generirajTekstualniReplay(String putanjaDatoteke) {
        Map<Integer, List<PodatakOPotezu>> potPoRundama = ucitajIGrupirajPoRundama(putanjaDatoteke);
        StringBuilder tekstReplaya = new StringBuilder();

        for (Map.Entry<Integer, List<PodatakOPotezu>> unosRunde : potPoRundama.entrySet()) {
            tekstReplaya.append("--- Runda ").append(unosRunde.getKey()).append(" ---\n");
            for (PodatakOPotezu podatak : unosRunde.getValue()) {
                tekstReplaya.append(podatak.toString()).append("\n");
            }
        }

        return tekstReplaya.toString();
    }

    public List<PodatakOPotezu> ucitajSvePotezeRedom(String putanjaDatoteke) {
        List<PodatakOPotezu> svilPotezi = new ArrayList<>();
        Map<Integer, List<PodatakOPotezu>> potPoRundama = ucitajIGrupirajPoRundama(putanjaDatoteke);

        for (List<PodatakOPotezu> potezijedneRunde : potPoRundama.values()) {
            svilPotezi.addAll(potezijedneRunde);
        }

        return svilPotezi;
    }
}