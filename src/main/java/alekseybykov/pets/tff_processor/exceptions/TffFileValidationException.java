package alekseybykov.pets.tff_processor.exceptions;

public class TffFileValidationException extends RuntimeException {

    private TffFileValidationException(String message) {
        super(message);
    }

    public static TffFileValidationException missingSection(String sectionMarker) {
        return new TffFileValidationException(
                String.format("Указанная в схеме секция %s не найдена в ТФФ файле", sectionMarker)
        );
    }

    public static TffFileValidationException wrongFieldsNumber() {
        return new TffFileValidationException("Число секций ТФФ файла не соответствует схеме");
    }

    public static TffFileValidationException wrongDataType(String fieldName) {
        return new TffFileValidationException(
                String.format("Тип данных поля %s не соответствует схеме", fieldName)
        );
    }

    public static TffFileValidationException mandatoryFieldIsEmpty(String fieldName) {
        return new TffFileValidationException(
                String.format("Поле %s обязательно для заполнения", fieldName)
        );
    }
}
