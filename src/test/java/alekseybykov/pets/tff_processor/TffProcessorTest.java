package alekseybykov.pets.tff_processor;

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

class TffProcessorTest {

    private static final String TFF_ROOT_PATH = "alekseybykov/pets/tff_processor/tff_files";

    private static final String TFF_SCHEME_NAME = "TXUF180101.xml";
    private static final String TFF_FILE_NAME = "4330127_25014101.UF1";

    private static ITffProcessor tffProcessor;

    @BeforeAll
    public static void init() {
        tffProcessor = new TffProcessor(
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
        assertEquals("UF", tff.getMarker());
    }

    private void testVersion(TffFile tff) {
        assertEquals("TXUF180101", tff.getVersion());
    }

    private void testDocumentsCount(TffFile tff) {
        List<TffDocument> documents = tff.getDocuments();
        assertEquals(1, documents.size());
    }

    private void testTablesCount(TffFile tff) {
        List<TffDocument> documents = tff.getDocuments();

        TffDocument firstDocument = documents.getFirst();
        List<TffTable> firstDocumentTables = firstDocument.getTables();
        assertEquals(1, firstDocumentTables.size());

        TffDocument lastDocument = documents.getLast();
        List<TffTable> lastDocumentTables = lastDocument.getTables();
        assertEquals(1, lastDocumentTables.size());
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
        assertEquals(4, fields.size());

        assertEquals("TXUF180101", fk.getFieldValueByName("NUM_VER"));
        assertEquals("123", fk.getFieldValueByName("FORMER"));
        assertEquals("6.01В", fk.getFieldValueByName("FORM_VER"));
        assertEquals(StringUtils.EMPTY, fk.getFieldValueByName("NORM_DOC"));
    }

    private void testFromSection(TffFile tff) {
        TffMetadata from = tff.findMetadataByMarker(SectionType.FROM.getLiteral());
        assertEquals(from.getMarker(), SectionType.FROM.getLiteral());

        List<TffField> fields = from.getFields();
        assertEquals(3, fields.size());

        assertEquals("1", from.getFieldValueByName("BUDG_LEVEL"));
        assertEquals("12319101", from.getFieldValueByName("KOD_UBP"));
        assertEquals("321", from.getFieldValueByName("NAME_UBP"));
    }

    private void testToSection(TffFile tff) {
        TffMetadata to = tff.findMetadataByMarker(SectionType.TO.getLiteral());
        assertEquals(to.getMarker(), SectionType.TO.getLiteral());

        List<TffField> fields = to.getFields();
        assertEquals(2, fields.size());

        assertEquals("0200", to.getFieldValueByName("KOD_TOFK"));
        assertEquals("123", to.getFieldValueByName("NAME_TOFK"));
    }

    @Test
    public void testConvertTffToPlainStringAgainstXmlScheme() {
        TffFile tff = new TffFile();

        tff.setMarker("UF");
        tff.setVersion("TXUF180101");

        tff.getMetadata().add(
                TffMetadata.builder()
                        .marker("FK")
                        .field("NUM_VER", "TXUF180101")
                        .field("FORMER", "123")
                        .field("FORM_VER", "6.01В")
                        .field("NORM_DOC", "")
                        .build()
        );
        tff.getMetadata().add(
                TffMetadata.builder()
                        .marker("FROM")
                        .field("BUDG_LEVEL", "1")
                        .field("KOD_UBP", "12319101")
                        .field("NAME_UBP", "321")
                        .build()
        );
        tff.getMetadata().add(
                TffMetadata.builder()
                        .marker("TO")
                        .field("KOD_TOFK", "0200")
                        .field("NAME_TOFK", "123")
                        .build()
        );
        tff.getMetadata().add(
                TffMetadata.builder()
                        .marker("SECURE")
                        .field("LEVEL", "0")
                        .field("CAUSE", "")
                        .build()
        );

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
        return new TffDocument(newTffHeader());
    }

    private TffHeader newTffHeader() {
        return TffHeader.builder()
                .marker("UF")
                .field("GUID_FK", "")
                .field("NOM_UF", "15")
                .field("DATE_UF", "12.06.2021")
                .field("NAME_UBP", "12-23")
                .field("KOD_UBP", "02119101")
                .field("LS_UBP", "03111911010")
                .field("NAME_GRS", "123")
                .field("GLAVA_GRS", "101")
                .field("NAME_BUD", "111")
                .field("NAME_UBP_FO", "222")
                .field("OKPO_FO", "12345678")
                .field("LS_FO", "")
                .field("NAME_TOFK", "333")
                .field("KOD_TOFK", "0200")
                .field("FUND_SOURCE", "1")
                .field("NOM_ZF", "")
                .field("DATE_ZF", "11.06.2021")
                .field("CNAME_PL", "444")
                .field("INN_PL", "4501011121")
                .field("KPP_PL", "450101001")
                .field("PASP", "")
                .field("BS_PL", "")
                .field("DOL_RUK", "555")
                .field("NAME_RUK", "66")
                .field("DOL_ISP", "77")
                .field("NAME_ISP", "88")
                .field("TEL_ISP", "999-54-23")
                .field("DATE_POD", "11.06.2021")
                .field("DOL_RUK_TOFK", "")
                .field("NAME_RUK_TOFK", "")
                .field("DOL_ISP_TOFK", "")
                .field("NAME_ISP_TOFK", "")
                .field("TEL_ISP_TOFK", "")
                .field("DATE_TOFK", "")
                .build();
    }

    private TffTable newTffTable() {
        return TffTable.builder()
                .marker("UFPP")
                .field("LINE_NOM", "1")
                .field("GUID", "6F9619FF-8B86-D011-B42D-00C04FC964F1")
                .field("KOD_DOC", "ZR")
                .field("NAME_PP", "12")
                .field("NOM_PP", "015")
                .field("DATE_PP", "12.05.2021")
                .field("CNAME_PP", "56")
                .field("INN_PP", "4501011121")
                .field("KPP_PP", "450101001")
                .field("OKATO", "88700000")
                .field("KBK", "18301171200001001112")
                .field("TYPE_KBK", "10")
                .field("ADD_KLASS", "")
                .field("SUM_PP", "5000.00")
                .field("PURPOSE", "888")
                .field("NOM_ZR", "")
                .field("DATE_ZR", "")
                .field("NOTE", "")
                .build();
    }
}
