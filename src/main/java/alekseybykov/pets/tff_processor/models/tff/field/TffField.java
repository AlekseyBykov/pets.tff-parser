package alekseybykov.pets.tff_processor.models.tff.field;

import java.util.Objects;

public class TffField {

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffField tffField = (TffField) o;

        return Objects.equals(name, tffField.name) &&
                Objects.equals(value, tffField.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                value
        );
    }
}
