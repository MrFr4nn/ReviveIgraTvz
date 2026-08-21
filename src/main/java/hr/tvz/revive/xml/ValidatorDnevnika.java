package hr.tvz.revive.xml;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

public class ValidatorDnevnika {

    private static final String PUTANJA_XSD_SHEME = "/hr/tvz/revive/revive-log.xsd";

    public boolean validirajDnevnik(String putanjaXmlDatoteke) {
        try {
            SchemaFactory tvornicaShema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema shema = tvornicaShema.newSchema(getClass().getResource(PUTANJA_XSD_SHEME));

            Validator validator = shema.newValidator();
            validator.validate(new StreamSource(new File(putanjaXmlDatoteke)));

            return true;
        } catch (SAXException | IOException iznimka) {
            System.out.println("Dnevnik nije valjan prema XSD shemi: " + iznimka.getMessage());
            return false;
        }
    }
}