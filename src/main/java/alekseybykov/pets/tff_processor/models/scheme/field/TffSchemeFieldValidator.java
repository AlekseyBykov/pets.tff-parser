package alekseybykov.pets.tff_processor.models.scheme.field;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class TffSchemeFieldValidator {

    private Class<?> type;
    private Double length;
    private String condition;

    public Class<?> getType() {
        return type;
    }

    public void setType(String type) {
        if (StringUtils.equals("string", type)) {
            this.type = String.class;
        }

        if (StringUtils.equals("integer", type)) {
            this.type = String.class;
        }

        if (StringUtils.equals("double", type)) {
            this.type = String.class;
        }

        if (StringUtils.equals("guid", type)) {
            this.type = String.class;
        }

        if (StringUtils.equals("date", type)) {
            this.type = String.class;
        }
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffSchemeFieldValidator validator = (TffSchemeFieldValidator) o;

        return Objects.equals(type, validator.type) &&
                Objects.equals(length, validator.length) &&
                Objects.equals(condition, validator.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                type,
                length,
                condition
        );
    }
}
