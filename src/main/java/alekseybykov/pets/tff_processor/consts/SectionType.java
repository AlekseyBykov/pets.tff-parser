package alekseybykov.pets.tff_processor.consts;

public enum SectionType {

    FK("FK"),
    FROM("FROM"),
    TO("TO"),
    SECURE("SECURE");

    private final String literal;

    SectionType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
