package alekseybykov.pets.tff_processor;

import alekseybykov.pets.tff_processor.test_utils.ResourceFileLoader;
import alekseybykov.pets.tff_processor.validators.xsd.impl.XSDValidator;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TffSchemesValidationTest {

    private static final String TFF_SCHEME_ROOT_PATH = "alekseybykov/pets/tff_processor/tff_schemes";
    private static final String XSD_SCHEME_ROOT_PATH = "alekseybykov/pets/tff_processor/xsd";

    @Test
    public void testValidateTffSchemeAgainstXsdScheme() throws Exception  {
        File[] schemes = ResourceFileLoader.loadFiles(TFF_SCHEME_ROOT_PATH);
        File xsd = ResourceFileLoader.loadFile(
                XSD_SCHEME_ROOT_PATH,
                "TffSchemes.xsd"
        );

        XSDValidator validator = XSDValidator.newXSDValidator();
        Stream.of(schemes)
                .forEach(scheme -> validator.validate(scheme, xsd));

        assertTrue(
                CollectionUtils.isEmpty(
                        validator.getErrorMessages()
                )
        );
    }
}
