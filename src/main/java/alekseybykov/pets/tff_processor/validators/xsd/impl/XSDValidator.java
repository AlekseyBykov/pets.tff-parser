package alekseybykov.pets.tff_processor.validators.xsd.impl;

import alekseybykov.pets.tff_processor.validators.xsd.IXSDValidator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XSDValidator implements IXSDValidator {

    private static final Boolean INVALID_SCHEME = Boolean.FALSE;
    private static final Boolean VALID_SCHEME = Boolean.TRUE;

    private final List<String> errorMessages = new ArrayList<>();

    public static XSDValidator newXSDValidator() {
        return new XSDValidator();
    }

    @Override
    public boolean isValidScheme(File xml, File xsd) throws IOException, SAXException {
        Validator validator = initValidator(xsd);

        XSDErrorHandler errorHandler = XSDErrorHandler.newXSDErrorHandler();
        validator.setErrorHandler(errorHandler);
        try {
            validator.validate(new StreamSource(xml));
            prepareErrorMessages(errorHandler, xml);

            return VALID_SCHEME;
        } catch (SAXException e) {
            return INVALID_SCHEME;
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private Validator initValidator(File xsd) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Source schemaFile = new StreamSource(xsd);
        Schema schema = factory.newSchema(schemaFile);

        return schema.newValidator();
    }

    private void prepareErrorMessages(
            XSDErrorHandler errorHandler,
            File scheme
    ) {
        List<SAXParseException> exceptions = errorHandler.getExceptions();
        exceptions.stream()
                .map(e -> String.format(
                        "ТФФ схема %s не соответствует схеме XSD, line: %s, col: %s. %s",
                        scheme.getAbsolutePath(),
                        e.getLineNumber(),
                        e.getColumnNumber(),
                        e.getMessage()
                )).forEach(errorMessages::add);
    }

    private static class XSDErrorHandler implements ErrorHandler {
        private final List<SAXParseException> exceptions = new ArrayList<>();

        public static XSDErrorHandler newXSDErrorHandler() {
            return new XSDErrorHandler();
        }

        @Override
        public void warning(SAXParseException exception) {
            exceptions.add(exception);
        }

        @Override
        public void error(SAXParseException exception) {
            exceptions.add(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) {
            exceptions.add(exception);
        }

        public List<SAXParseException> getExceptions() {
            return exceptions;
        }
    }
}
