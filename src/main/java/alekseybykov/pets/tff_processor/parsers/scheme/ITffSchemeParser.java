package alekseybykov.pets.tff_processor.parsers.scheme;

import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;

import java.io.File;
import java.io.InputStream;

public interface ITffSchemeParser {

    TffSchemeFile parse(File file);

    TffSchemeFile parse(InputStream inputStream);
}
