package alekseybykov.pets.tff_processor.naming;

import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;
import alekseybykov.pets.tff_processor.models.tff.TffFile;

public interface ITffNamingService {

    void setFieldNames(TffFile tffFile, TffSchemeFile tffSchema);
}
