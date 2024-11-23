package alekseybykov.pets.tff_processor.models.tff.metadata;

import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffMetadata {

    private String marker;
    private List<TffField> fields = new ArrayList<>();

    public TffMetadata() {
    }

    TffMetadata(String marker, List<TffField> fields) {
        this.marker = marker;
        this.fields = fields;
    }

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

    public static Builder builder() {
        return new Builder();
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

    public void addField(String name, Object value) {
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

    public static class Builder {
        private String marker;
        private final List<TffField> fields = new ArrayList<>();

        Builder() {
        }

        public Builder marker(String marker) {
            this.marker = marker;
            return this;
        }

        public Builder field(String name, Object value) {
            TffField field = new TffField();
            field.setName(name);
            field.setValue(Objects.toString(value, StringUtils.EMPTY));
            fields.add(field);

            return this;
        }

        public TffMetadata build() {
            return new TffMetadata(this.marker, this.fields);
        }
    }
}
