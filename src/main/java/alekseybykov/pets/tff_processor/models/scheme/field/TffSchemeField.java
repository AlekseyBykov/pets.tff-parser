package alekseybykov.pets.tff_processor.models.scheme.field;

import java.util.Objects;

public class TffSchemeField {

    private String name;
    private int position;
    private boolean mandatory;

    private TffSchemeFieldValidator validator;

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TffSchemeFieldValidator getValidator() {
        return validator;
    }

    public void setValidator(TffSchemeFieldValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffSchemeField that = (TffSchemeField) o;

        return position == that.position &&
                mandatory == that.mandatory &&
                Objects.equals(name, that.name) &&
                Objects.equals(validator, that.validator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                position,
                mandatory,
                validator
        );
    }
}
