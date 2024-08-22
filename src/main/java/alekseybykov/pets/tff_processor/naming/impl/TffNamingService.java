package alekseybykov.pets.tff_processor.naming.impl;

import alekseybykov.pets.tff_processor.models.scheme.IndexedScheme;
import alekseybykov.pets.tff_processor.models.scheme.TffSchemeFile;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeDocument;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeHeader;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeTable;
import alekseybykov.pets.tff_processor.models.scheme.field.TffSchemeField;
import alekseybykov.pets.tff_processor.models.scheme.metadata.TffSchemeMetadata;
import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.data.TffTable;
import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import alekseybykov.pets.tff_processor.naming.ITffNamingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TffNamingService implements ITffNamingService {

    @Override
    public void setFieldNames(
            TffFile tff,
            TffSchemeFile schema
    ) {
        setMetadataFieldNames(tff, schema);
        setDataFieldNames(tff, schema);
    }

    private void setMetadataFieldNames(
            TffFile tff,
            TffSchemeFile schema
    ) {
        List<TffSchemeMetadata> metadataSchemes = schema.getMetadataSchemes();
        for (TffSchemeMetadata metadataScheme : metadataSchemes) {
            var marker = metadataScheme.getMarker();
            TffMetadata metadata = tff.findMetadataByMarker(marker);

            setFieldNames(metadataScheme, metadata.getFields());
        }
    }

    private void setDataFieldNames(
            TffFile tff,
            TffSchemeFile schema
    ) {
        TffSchemeHeader schemeHeader = schema.getData().getHeader();
        for (TffDocument document : tff.getDocuments()) {
            setDocumentHeaderFieldNames(document, schemeHeader);
            setNestedTableFieldNames(document, schema.getData());
        }
    }

    private void setNestedTableFieldNames(
            TffDocument document,
            TffSchemeDocument schema
    ) {
        for (TffTable table : document.getTables()) {
            TffSchemeTable schemeTable = schema.findTableByMarker(table.getMarker());
            setFieldNames(schemeTable, table.getFields());
        }
    }

    private void setDocumentHeaderFieldNames(
            TffDocument document,
            TffSchemeHeader scheme
    ) {
        var fields = document.getHeader().getFields();
        setFieldNames(scheme, fields);
    }

    private void setFieldNames(
            IndexedScheme scheme,
            List<TffField> fields
    ) {
        for (int i = 0; i < fields.size(); i++) {
            TffField field = fields.get(i);
            TffSchemeField schemeField = scheme.findFieldByPosition(i + 1);
            field.setName(schemeField.getName());
        }
    }
}
