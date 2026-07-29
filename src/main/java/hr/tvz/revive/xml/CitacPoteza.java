package hr.tvz.revive.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CitacPoteza {

    public List<PodatakOPotezu> procitajPoteze(String putanjaDatoteke) {
        List<PodatakOPotezu> procitaniPotezi = new ArrayList<>();

        try {
            SAXParserFactory tvornicaParsera = SAXParserFactory.newInstance();
            SAXParser saxParser = tvornicaParsera.newSAXParser();

            RukovatelJPotezima rukovatelj = new RukovatelJPotezima(procitaniPotezi);
            saxParser.parse(new File(putanjaDatoteke), rukovatelj);

        } catch (ParserConfigurationException | SAXException | IOException iznimka) {
            System.out.println("Greska prilikom citanja XML dnevnika: " + iznimka.getMessage());
        }

        return procitaniPotezi;
    }


    private static class RukovatelJPotezima extends DefaultHandler {

        private List<PodatakOPotezu> procitaniPotezi;

        RukovatelJPotezima(List<PodatakOPotezu> procitaniPotezi) {
            this.procitaniPotezi = procitaniPotezi;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atributi) {
            if (qName.equals("potez")) {
                int brojRunde = Integer.parseInt(atributi.getValue("runda"));
                String imeIgraca = atributi.getValue("igrac");
                String nazivKarte = atributi.getValue("karta");
                String tipRadnika = atributi.getValue("radnik");

                procitaniPotezi.add(new PodatakOPotezu(brojRunde, imeIgraca, nazivKarte, tipRadnika));
            }
        }
    }
}