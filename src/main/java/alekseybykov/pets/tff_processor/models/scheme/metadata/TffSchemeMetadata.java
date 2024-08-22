package alekseybykov.pets.tff_processor.models.scheme.metadata;

import alekseybykov.pets.tff_processor.exceptions.TffSchemeParseException;
import alekseybykov.pets.tff_processor.models.scheme.IndexedScheme;
import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;

import java.util.ArrayList;
import java.util.List;

public class TffSchemeMetadata implements IndexedScheme {

    private String marker;
    private List<TffSchemeField> fields = new ArrayList<>();

    @Override
    public TffSchemeField findFieldByPosition(int position) {
        return fields.stream()
                .filter(tffField -> tffField.getPosition() == position)
                .findFirst()
                .orElseThrow(
                        () -> TffSchemeParseException.missingField(position)
                );
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return marker;
    }

    public List<TffSchemeField> getFields() {
        return fields;
    }

    public void setFields(List<TffSchemeField> fields) {
        this.fields = fields;
    }
}
