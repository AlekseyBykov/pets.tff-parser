package alekseybykov.pets.tff_processor.models.scheme;

import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;

public interface IndexedScheme {

    TffSchemeField findFieldByPosition(int position);
}
