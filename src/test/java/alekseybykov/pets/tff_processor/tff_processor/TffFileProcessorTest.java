package alekseybykov.pets.tff_processor.tff_processor;

import alekseybykov.pets.tff_processor.ITffFileProcessor;
import alekseybykov.pets.tff_processor.TffFileProcessor;
import alekseybykov.pets.tff_processor.consts.SectionType;
import alekseybykov.pets.tff_processor.models.tff.TffFile;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.data.TffHeader;
import alekseybykov.pets.tff_processor.models.tff.data.TffTable;
import alekseybykov.pets.tff_processor.models.tff.field.TffField;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import alekseybykov.pets.tff_processor.naming.impl.TffNamingService;
import alekseybykov.pets.tff_processor.parsers.scheme.impl.TffSchemeParser;
import alekseybykov.pets.tff_processor.parsers.tff.impl.TffFileParser;
import alekseybykov.pets.tff_processor.serializer.impl.TffSerializer;
import alekseybykov.pets.tff_processor.test_utils.ResourceFileLoader;
import alekseybykov.pets.tff_processor.validators.tff.impl.TffFileValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TffFileProcessorTest {

    private static final String TFF_ROOT_PATH = "alekseybykov/pets/tff_processor/tff_files";

    private static final String TFF_SCHEME_NAME = "TXUF180101.xml";
    private static final String TFF_FILE_NAME = "4330127_25014101.UF1";

    private static ITffFileProcessor tffProcessor;

    @BeforeAll
    public static void init() {
        tffProcessor = new TffFileProcessor(
                new TffSchemeParser(),
                new TffFileParser(),
                new TffFileValidator(),
                new TffNamingService(),
                new TffSerializer()
        );
    }

    @Test
    public void testParseTffTextFileAgainstXmlScheme() throws IOException {
        MultipartFile file = ResourceFileLoader.loadMultipartFile(
                TFF_ROOT_PATH,
                TFF_FILE_NAME
        );

        TffFile tff = tffProcessor.process(file, "UF", TFF_SCHEME_NAME);

        testMarker(tff);
        testVersion(tff);

        testDocumentsCount(tff);
        testTablesCount(tff);

        testMetadata(tff);
    }

    private void testMarker(TffFile tff) {
        assertEquals(tff.getMarker(), "UF");
    }

    private void testVersion(TffFile tff) {
        assertEquals(tff.getVersion(), "TXUF180101");
    }

    private void testDocumentsCount(TffFile tff) {
        List<TffDocument> documents = tff.getDocuments();
        assertEquals(documents.size(), 1);
    }

    private void testTablesCount(TffFile tff) {
        List<TffDocument> documents = tff.getDocuments();

        TffDocument firstDocument = documents.getFirst();
        List<TffTable> firstDocumentTables = firstDocument.getTables();
        assertEquals(firstDocumentTables.size(), 1);

        TffDocument lastDocument = documents.getLast();
        List<TffTable> lastDocumentTables = lastDocument.getTables();
        assertEquals(lastDocumentTables.size(), 1);
    }

    private void testMetadata(TffFile tff) {
        testFkSection(tff);
        testFromSection(tff);
        testToSection(tff);
    }

    private void testFkSection(TffFile tff) {
        TffMetadata fk = tff.findMetadataByMarker(SectionType.FK.getLiteral());
        assertEquals(fk.getMarker(), SectionType.FK.getLiteral());

        List<TffField> fields = fk.getFields();
        assertEquals(fields.size(), 4);

        assertEquals(fk.getFieldValueByName("NUM_VER"), "TXUF180101");
        assertEquals(fk.getFieldValueByName("FORMER"), "123");
        assertEquals(fk.getFieldValueByName("FORM_VER"), "6.01В");
        assertEquals(fk.getFieldValueByName("NORM_DOC"), StringUtils.EMPTY);
    }

    private void testFromSection(TffFile tff) {
        TffMetadata from = tff.findMetadataByMarker(SectionType.FROM.getLiteral());
        assertEquals(from.getMarker(), SectionType.FROM.getLiteral());

        List<TffField> fields = from.getFields();
        assertEquals(fields.size(), 3);

        assertEquals(from.getFieldValueByName("BUDG_LEVEL"), "1");
        assertEquals(from.getFieldValueByName("KOD_UBP"), "12319101");
        assertEquals(from.getFieldValueByName("NAME_UBP"), "321");
    }

    private void testToSection(TffFile tff) {
        TffMetadata to = tff.findMetadataByMarker(SectionType.TO.getLiteral());
        assertEquals(to.getMarker(), SectionType.TO.getLiteral());

        List<TffField> fields = to.getFields();
        assertEquals(fields.size(), 2);

        assertEquals(to.getFieldValueByName("KOD_TOFK"), "0200");
        assertEquals(to.getFieldValueByName("NAME_TOFK"), "123");
    }

    @Test
    public void testConvertTffToPlainStringAgainstXmlScheme() {
        TffFile tff = new TffFile();

        tff.setMarker("UF");
        tff.setVersion("TXUF180101");

        TffMetadata fk = new TffMetadata();
        fk.setMarker("FK");

        fk.addField("NUM_VER", "TXUF180101");
        fk.addField("FORMER", "123");
        fk.addField("FORM_VER", "6.01В");
        fk.addField("NORM_DOC", "");

        tff.getMetadata().add(fk);

        TffMetadata from = new TffMetadata();
        from.setMarker("FROM");

        from.addField("BUDG_LEVEL", "1");
        from.addField("KOD_UBP", "12319101");
        from.addField("NAME_UBP", "321");

        tff.getMetadata().add(from);

        TffMetadata to = new TffMetadata();
        to.setMarker("TO");

        to.addField("KOD_TOFK", "0200");
        to.addField("NAME_TOFK", "123");

        tff.getMetadata().add(to);

        TffMetadata secure = new TffMetadata();
        secure.setMarker("SECURE");

        secure.addField("LEVEL", "0");
        secure.addField("CAUSE", "");

        tff.getMetadata().add(secure);

        TffTable firstUfPPTable = newTffTable();

        TffDocument firstDocument = newTffDocument();
        firstDocument.getTables().add(firstUfPPTable);

        tff.getDocuments().add(firstDocument);

        var tffString = tffProcessor.toPlainText(tff);

        assertEquals(
                "FK|TXUF180101|123|6.01В||\n" +
                        "FROM|1|12319101|321|\n" +
                        "TO|0200|123|\n" +
                        "SECURE|0||\n" +
                        "UF||15|12.06.2021|12-23|02119101|03111911010|123|101|111|222|12345678||333|0200|1||" +
                           "11.06.2021|444|4501011121|450101001|||555|66|77|88|999-54-23|11.06.2021|||||||\n" +
                        "UFPP|1|6F9619FF-8B86-D011-B42D-00C04FC964F1|ZR|12|015|12.05.2021|56|4501011121|450101001|" +
                           "88700000|18301171200001001112|10||5000.00|888||||\n",
                tffString
        );
    }

    private TffDocument newTffDocument() {
        TffDocument document = new TffDocument();

        TffHeader header = new TffHeader();
        header.setMarker("UF");

        header.addField("GUID_FK", "");
        header.addField("NOM_UF", "15");
        header.addField("DATE_UF", "12.06.2021");
        header.addField("NAME_UBP", "12-23");
        header.addField("KOD_UBP", "02119101");
        header.addField("LS_UBP", "03111911010");
        header.addField("NAME_GRS", "123");
        header.addField("GLAVA_GRS", "101");
        header.addField("NAME_BUD", "111");
        header.addField("NAME_UBP_FO", "222");
        header.addField("OKPO_FO", "12345678");
        header.addField("LS_FO", "");
        header.addField("NAME_TOFK", "333");
        header.addField("KOD_TOFK", "0200");
        header.addField("FUND_SOURCE", "1");
        header.addField("NOM_ZF", "");
        header.addField("DATE_ZF", "11.06.2021");
        header.addField("CNAME_PL", "444");
        header.addField("INN_PL", "4501011121");
        header.addField("KPP_PL", "450101001");
        header.addField("PASP", "");
        header.addField("BS_PL", "");
        header.addField("DOL_RUK", "555");
        header.addField("NAME_RUK", "66");
        header.addField("DOL_ISP", "77");
        header.addField("NAME_ISP", "88");
        header.addField("TEL_ISP", "999-54-23");
        header.addField("DATE_POD", "11.06.2021");
        header.addField("DOL_RUK_TOFK", "");
        header.addField("NAME_RUK_TOFK", "");
        header.addField("DOL_ISP_TOFK", "");
        header.addField("NAME_ISP_TOFK", "");
        header.addField("TEL_ISP_TOFK", "");
        header.addField("DATE_TOFK", "");

        document.setHeader(header);

        return document;
    }

    private TffTable newTffTable() {
        TffTable table = new TffTable();
        table.setMarker("UFPP");

        table.addField("LINE_NOM", "1");
        table.addField("GUID", "6F9619FF-8B86-D011-B42D-00C04FC964F1");
        table.addField("KOD_DOC", "ZR");
        table.addField("NAME_PP", "12");
        table.addField("NOM_PP", "015");
        table.addField("DATE_PP", "12.05.2021");
        table.addField("CNAME_PP", "56");
        table.addField("INN_PP", "4501011121");
        table.addField("KPP_PP", "450101001");
        table.addField("OKATO", "88700000");
        table.addField("KBK", "18301171200001001112");
        table.addField("TYPE_KBK", "10");
        table.addField("ADD_KLASS", "");
        table.addField("SUM_PP", "5000.00");
        table.addField("PURPOSE", "888");
        table.addField("NOM_ZR", "");
        table.addField("DATE_ZR", "");
        table.addField("NOTE", "");

        return table;
    }
}
