package alekseybykov.pets.tff_processor.exceptions;

public class TffSchemeParseException extends RuntimeException {

    private TffSchemeParseException(String message) {
        super(message);
    }

    public static TffSchemeParseException missingField(int position) {
        return new TffSchemeParseException(
                String.format("Поле схемы не найдено в указанной позиции %s", position)
        );
    }

    public static TffSchemeParseException missingSection(String sectionMarker) {
        return new TffSchemeParseException(
                String.format("Секция схемы не найдена по маркеру %s", sectionMarker)
        );
    }
}
