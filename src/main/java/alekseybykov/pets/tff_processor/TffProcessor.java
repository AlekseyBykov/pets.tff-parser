package alekseybykov.pets.tff_processor;

import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;
import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.naming.ITffNamingService;
import alekseybykov.pets.tff_processor.parsers.scheme.ITffSchemeParser;
import alekseybykov.pets.tff_processor.parsers.tff.ITffFileParser;
import alekseybykov.pets.tff_processor.serializer.ITffSerializer;
import alekseybykov.pets.tff_processor.validators.tff.ITffFileValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class TffProcessor implements ITffProcessor {

    private static final String RESOURCE_PATH = "/alekseybykov/pets/tff_processor/tff_schemes";

    private final ITffSchemeParser schemeParser;
    private final ITffFileParser fileParser;
    private final ITffFileValidator validator;
    private final ITffNamingService namingService;
    private final ITffSerializer serializer;

    public TffProcessor(
            ITffSchemeParser schemeParser,
            ITffFileParser fileParser,
            ITffFileValidator validator,
            ITffNamingService namingService,
            ITffSerializer serializer
    ) {
        this.schemeParser = schemeParser;
        this.fileParser = fileParser;
        this.validator = validator;
        this.namingService = namingService;
        this.serializer = serializer;
    }

    @Override
    public TffFile process(MultipartFile file, String documentMarker, String schemeName) {
        try (
                var schemeInputStream = loadFile(schemeName);
                var tffInputStream = file.getInputStream()
        ) {
            TffSchemeFile tffSchema = schemeParser.parse(schemeInputStream);
            TffFile tffFile = fileParser.parse(tffInputStream, documentMarker);

            validator.validate(tffFile, tffSchema);

            namingService.setFieldNames(tffFile, tffSchema);

            return tffFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toPlainText(TffFile tff) {
        return toString(tff);
    }

    @Override
    public byte[] toByteArray(TffFile tff, Charset charset) {
        return toString(tff).getBytes(charset);
    }

    private String toString(TffFile tff) {
        return serializer.serialize(tff);
    }

    private InputStream loadFile(String fileName) {
        return getClass().getResourceAsStream(
                String.format("%s/%s", RESOURCE_PATH, fileName)
        );
    }
}
