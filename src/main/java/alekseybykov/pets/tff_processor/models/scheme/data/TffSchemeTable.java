package alekseybykov.pets.tff_processor.models.scheme.data;

import alekseybykov.pets.tff_processor.models.scheme.IndexedScheme;
import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffSchemeTable implements IndexedScheme {

    private String marker;
    private List<TffSchemeField> fields = new ArrayList<>();

    @Override
    public TffSchemeField findFieldByPosition(int position) {
        return fields.stream()
                .filter(tffField -> tffField.getPosition() == position)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
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

        TffSchemeTable that = (TffSchemeTable) o;

        return Objects.equals(marker, that.marker) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                marker,
                fields
        );
    }
}
