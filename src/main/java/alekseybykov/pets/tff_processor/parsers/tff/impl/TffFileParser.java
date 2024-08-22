package alekseybykov.pets.tff_processor.parsers.tff.impl;

import alekseybykov.pets.tff_processor.consts.SectionType;
import alekseybykov.pets.tff_processor.exceptions.TffFileParseException;
import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.data.TffHeader;
import alekseybykov.pets.tff_processor.models.tff.data.TffTable;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import alekseybykov.pets.tff_processor.parsers.tff.ITffFileParser;
import alekseybykov.pets.tff_processor.utils.TffSplitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class TffFileParser implements ITffFileParser {

    private static final String TFF_ENCODING = "Windows-1251";

    @Override
    public TffFile parse(File file, String documentMarker) {
        try (InputStream is = new FileInputStream(file)) {
            return parse(is, documentMarker);
        } catch (Exception e) {
            throw new TffFileParseException(e.getMessage());
        }
    }

    @Override
    public TffFile parse(
            InputStream inputStream,
            String documentMarker
    ) {
        TffFile tff = new TffFile();

        TffDocument document = null;

        tff.setMarker(documentMarker);

        int markerPos = 0;
        int versionPos = 1;

        Scanner scanner = newScanner(inputStream);
        while (scanner.hasNextLine()) {
            var section = scanner.nextLine();
            var sectionFields = Arrays.asList(TffSplitter.split(section));
            var rowMarker = sectionFields.get(markerPos);

            if (StringUtils.equals(SectionType.FK.getLiteral(), rowMarker)) {
                tff.setVersion(sectionFields.get(versionPos));
            }

            if (Arrays.asList(SectionType.FK.getLiteral(), SectionType.FROM.getLiteral(),
                    SectionType.TO.getLiteral(), SectionType.SECURE.getLiteral()
            ).contains(rowMarker)) {
                TffMetadata metadata = TffMetadata.newMetadata(sectionFields, rowMarker);
                tff.getMetadata().add(metadata);

            } else if (StringUtils.equals(documentMarker, rowMarker)) {
                document = new TffDocument();
                addTffHeader(document, sectionFields, documentMarker);

                tff.getDocuments().add(document);
            } else {
                TffTable table = TffTable.newTable(sectionFields, rowMarker);
                document.getTables().add(table);
            }
        }
        return tff;
    }

    private Scanner newScanner(InputStream tffInputStream) {
        return new Scanner(tffInputStream, Charset.forName(TFF_ENCODING));
    }

    private void addTffHeader(
            TffDocument document,
            List<String> fields,
            String marker
    ) {
        TffHeader header = TffHeader.newHeader(fields, marker);
        document.setHeader(header);
    }
}
