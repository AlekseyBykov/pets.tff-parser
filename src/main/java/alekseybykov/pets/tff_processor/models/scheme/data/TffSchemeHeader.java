package alekseybykov.pets.tff_processor.models.scheme.data;

import alekseybykov.pets.tff_processor.models.scheme.IndexedScheme;
import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffSchemeHeader implements IndexedScheme {

    private List<TffSchemeField> fields = new ArrayList<>();

    @Override
    public TffSchemeField findFieldByPosition(int position) {
        return fields.stream()
                .filter(tffField -> position == tffField.getPosition())
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public List<TffSchemeField> getFields() {
        return fields;
    }

    public void setFields(List<TffSchemeField> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffSchemeHeader that = (TffSchemeHeader) o;

        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                fields
        );
    }
}
