package alekseybykov.pets.tff_processor.serializer.impl;

import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.data.TffHeader;
import alekseybykov.pets.tff_processor.models.tff.data.TffTable;
import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import alekseybykov.pets.tff_processor.serializer.ITffSerializer;
import alekseybykov.pets.tff_processor.utils.CustomStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class TffSerializer implements ITffSerializer {

    private static final String TFF_DELIMITER = "|";
    private static final String TFF_EOL = "\n";

    @Override
    public String serialize(TffFile tff) {
        var sb = new CustomStringBuilder();

        var metadataString = serializeMetadata(tff);
        sb.append(metadataString);

        var documentString = serializeDocuments(tff);
        sb.append(documentString);

        return sb.toString();
    }

    private String serializeMetadata(TffFile tff) {
        var sb = new CustomStringBuilder();
        for (TffMetadata metada : tff.getMetadata()) {
            var marker = metada.getMarker();
            sb.append(marker).append(TFF_DELIMITER);

            metada.getFields().stream()
                    .map(TffField::getValue)
                    .forEach(val -> sb.append(val).append(TFF_DELIMITER));
            sb.append(TFF_EOL);
        }
        return sb.toString();
    }

    private String serializeDocuments(TffFile tff) {
        var sb = new CustomStringBuilder();
        for (TffDocument document : tff.getDocuments()) {

            var headerString = serializeHeader(document);
            sb.append(headerString);

            for (TffTable table : document.getTables()) {
                var tableString = serializeTable(table);
                sb.append(tableString);
            }
        }
        return sb.toString();
    }

    private String serializeHeader(TffDocument document) {
        var sb = new CustomStringBuilder();

        TffHeader header = document.getHeader();

        var marker = header.getMarker();
        sb.append(marker).append(TFF_DELIMITER);

        header.getFields().stream()
                .map(TffField::getValue)
                .forEach(val -> sb.append(val).append(TFF_DELIMITER));
        sb.append(TFF_EOL);

        return sb.toString();
    }

    private String serializeTable(TffTable table) {
        var sb = new CustomStringBuilder();

        var marker = table.getMarker();
        sb.append(marker).append(TFF_DELIMITER);

        table.getFields().stream()
                .map(TffField::getValue)
                .forEach(val -> sb.append(val).append(TFF_DELIMITER));
        sb.append(TFF_EOL);

        return sb.toString();
    }
}
