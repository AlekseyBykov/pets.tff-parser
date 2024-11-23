package alekseybykov.pets.tff_processor.models.tff.data;

import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffTable {

    private String marker;
    private List<TffField> fields = new ArrayList<>();

    public TffTable() {
    }

    public TffTable(String marker, List<TffField> fields) {
        this.marker = marker;
        this.fields = fields;
    }

    public static TffTable newTable(List<String> fields, String marker) {
        TffTable table = new TffTable();

        int valuePos = 1;
        for (int i = valuePos; i < fields.size(); i++) {
            TffField field = new TffField();

            var fieldValue = fields.get(i);
            field.setValue(fieldValue);
            table.getFields().add(field);
        }

        table.setMarker(marker);
        return table;
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

        TffTable tffTable = (TffTable) o;

        return Objects.equals(marker, tffTable.marker) &&
                Objects.equals(fields, tffTable.fields);
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

        public TffTable build() {
            return new TffTable(this.marker, this.fields);
        }
    }
}
