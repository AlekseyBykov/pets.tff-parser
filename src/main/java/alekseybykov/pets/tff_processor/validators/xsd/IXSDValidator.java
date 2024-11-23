package alekseybykov.pets.tff_processor.validators.xsd;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public interface IXSDValidator {

    void validate(File xml, File xsd);

    boolean isValidScheme(File xml, File xsd) throws Exception;
}
