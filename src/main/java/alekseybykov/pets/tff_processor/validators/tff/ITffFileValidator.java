package alekseybykov.pets.tff_processor.validators.tff;

import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;
import alekseybykov.pets.tff_processor.models.tff.TffFile;

public interface ITffFileValidator {

    void validate(TffFile tff, TffSchemeFile scheme);
}
