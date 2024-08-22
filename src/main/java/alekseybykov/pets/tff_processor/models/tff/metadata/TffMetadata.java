package alekseybykov.pets.tff_processor.models.tff.metadata;

import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffMetadata {

    private String marker;
    private List<TffField> fields = new ArrayList<>();

    public static TffMetadata newMetadata(
            List<String> fields,
            String marker
    ) {
        TffMetadata metadata = new TffMetadata();

        int valuePos = 1;
        for (int i = valuePos; i < fields.size(); i++) {
            TffField field = new TffField();

            var val = fields.get(i);
            field.setValue(val);
            metadata.getFields().add(field);
        }
        metadata.setMarker(marker);
        return metadata;
    }

    public String getFieldValueByName(String name) {
        TffField field = getFieldByName(name);
        return field != null
                ? field.getValue()
                : null;
    }

    public TffField getFieldByName(String name) {
        return fields.stream()
                .filter(field -> StringUtils.equals(field.getName(), name))
                .findFirst()
                .orElse(null);
    }

    public void addField(String name, String value) {
        TffField field = new TffField();
        field.setName(name);
        field.setValue(Objects.toString(value, StringUtils.EMPTY));

        fields.add(field);
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<TffField> getFields() {
        return fields;
    }

    public void setFields(List<TffField> fields) {
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

        TffMetadata metadata = (TffMetadata) o;

        return Objects.equals(marker, metadata.marker) &&
                Objects.equals(fields, metadata.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                marker,
                fields
        );
    }
}
