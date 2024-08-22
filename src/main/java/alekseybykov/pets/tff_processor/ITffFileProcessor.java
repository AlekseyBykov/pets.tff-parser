package alekseybykov.pets.tff_processor;

import alekseybykov.pets.tff_processor.models.tff.TffFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;

public interface ITffFileProcessor {

    TffFile process(MultipartFile file, String documentMarker, String schemeName);

    String toPlainText(TffFile tff);

    byte[] toByteArray(TffFile tff, Charset charset);
}
