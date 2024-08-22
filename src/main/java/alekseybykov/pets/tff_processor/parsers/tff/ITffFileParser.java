package alekseybykov.pets.tff_processor.parsers.tff;

import alekseybykov.pets.tff_processor.models.tff.TffFile;

import java.io.File;
import java.io.InputStream;

public interface ITffFileParser {

    TffFile parse(File file, String documentMarker);

    TffFile parse(InputStream inputStream, String documentMarker);
}
