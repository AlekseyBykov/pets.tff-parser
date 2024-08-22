package alekseybykov.pets.tff_processor.validators.tff.impl;

import alekseybykov.pets.tff_processor.exceptions.TffFileValidationException;
import alekseybykov.pets.tff_processor.models.scheme.IndexedScheme;
import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeDocument;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeHeader;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeTable;
import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;
import alekseybykov.pets.tff_processor.models.scheme.metadata.TffSchemeMetadata;
import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.data.TffHeader;
import alekseybykov.pets.tff_processor.models.tff.data.TffTable;
import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import alekseybykov.pets.tff_processor.validators.tff.ITffFileValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class TffFileValidator implements ITffFileValidator {

    @Override
    public void validate(TffFile tff, TffSchemeFile scheme) {
        validateMetadata(tff, scheme);
        validateData(tff, scheme);
    }

    private void validateMetadata(
            TffFile tff,
            TffSchemeFile scheme
    ) {
        for (TffSchemeMetadata metadataScheme : scheme.getMetadataSchemes()) {
            var sectionMarker = metadataScheme.getMarker();
            TffMetadata metadata = tff.findMetadataByMarker(sectionMarker);

            List<TffField> tffFields = metadata.getFields();

            validateFieldsNumber(metadataScheme.getFields(), tffFields);
            validateFieldValues(metadataScheme, tffFields);
        }
    }

    private void validateData(TffFile tff, TffSchemeFile scheme) {
        TffSchemeDocument dataScheme = scheme.getData();
        TffSchemeHeader headerScheme = dataScheme.getHeader();
        tff.getDocuments().forEach(document -> {
            validateDocument(document, headerScheme);
            validateNestedTable(document, dataScheme);
        });
    }

    private void validateDocument(TffDocument document, TffSchemeHeader headerScheme) {
        TffHeader header = document.getHeader();
        List<TffField> fields = header.getFields();

        IntStream.range(0, fields.size())
                .forEach(i ->
                        validateFieldValue(fields.get(i), headerScheme, i)
                );
    }

    private void validateNestedTable(
            TffDocument document,
            TffSchemeDocument dataScheme
    ) {
        for (TffTable table : document.getTables()) {
            var tableMarker = table.getMarker();
            TffSchemeTable tableScheme = dataScheme.findTableByMarker(tableMarker);

            List<TffField> tableFields = table.getFields();
            IntStream.range(0, tableFields.size())
                    .forEach(i ->
                            validateFieldValue(tableFields.get(i), tableScheme, i)
                    );
        }
    }

    private void validateFieldValues(
            TffSchemeMetadata metadataScheme,
            List<TffField> tffFields
    ) {
        IntStream.range(0, tffFields.size())
                .forEach(i ->
                        validateFieldValue(tffFields.get(i), metadataScheme, i)
                );
    }

    private void validateFieldValue(
            TffField field,
            IndexedScheme scheme,
            int fieldPos
    ) {
        TffSchemeField fieldScheme = scheme.findFieldByPosition(fieldPos + 1);

        checkFieldMandatory(field, fieldScheme);
        checkFieldDataType(field, fieldScheme);
    }

    private void checkFieldMandatory(
            TffField field,
            TffSchemeField fieldScheme
    ) {
        var val = field.getValue();
        if (fieldScheme.isMandatory() && StringUtils.isEmpty(val)) {
            throw TffFileValidationException.mandatoryFieldIsEmpty(fieldScheme.getName());
        }
    }

    private <T, U> void validateFieldsNumber(List<T> tList, List<U> uList) {
        if (tList.size() != uList.size()) {
            throw TffFileValidationException.wrongFieldsNumber();
        }
    }

    private void checkFieldDataType(
            TffField field,
            TffSchemeField fieldScheme
    ) {
        var val = field.getValue();
        if (StringUtils.isNotEmpty(val)) {
            var validator = fieldScheme.getValidator();
            Class<?> clazz = validator.getType();
            if (!val.getClass().equals(clazz)) {
                throw TffFileValidationException.wrongDataType(fieldScheme.getName());
            }
        }
    }
}
