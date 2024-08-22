package alekseybykov.pets.tff_processor.utils;

import java.util.Objects;

public class CustomStringBuilder {

    private final StringBuilder sb = new StringBuilder();

    public CustomStringBuilder append(String str) {
        sb.append(Objects.toString(str, ""));

        return this;
    }

    public CustomStringBuilder append(Object o) {
        sb.append(Objects.toString(o, ""));

        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
