package alekseybykov.pets.tff_processor.versioning;

import alekseybykov.pets.tff_processor.exceptions.TffSchemaNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

public enum TffSchemaVersion {

    /* since there are not many schemes, it can be versioned in enum */

    TXUF180101(
            "UF",
            "TXUF180101",
            "TXUF180101.xml",
            LocalDate.of(2024, Month.JANUARY, 1),
            DateBoundaries.UNLIMITED
    ),

    TXZF230101(
            "ZF",
            "TXZF230101",
            "TXZF230101.xml",
            LocalDate.of(2024, Month.FEBRUARY, 1),
            DateBoundaries.UNLIMITED
    );

    private final String marker;
    private final String version;
    private final String schemaName;

    private final LocalDate from;
    private final LocalDate to;

    TffSchemaVersion(
            String marker,
            String varsion,
            String schemaName,
            LocalDate from,
            LocalDate to
    ) {
        this.marker = marker;
        this.version = varsion;
        this.schemaName = schemaName;
        this.from = from;
        this.to = to;
    }

    public static TffSchemaVersion lookup(String marker) {
        return lookup(marker, LocalDate.now());
    }

    public static TffSchemaVersion lookup(String marker, LocalDate date) {
        return Arrays.stream(values())
                .filter(schema -> StringUtils.equals(marker, schema.marker))
                .filter(schema -> schema.to == null || !date.isAfter(schema.to))
                .findFirst()
                .orElseThrow(
                        () -> new TffSchemaNotFoundException(
                                String.format(
                                        "Не найдена актуальная на дату %s ТФФ схема для маркера %s",
                                        date,
                                        marker
                                ))
                );
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getMarker() {
        return marker;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    private static class DateBoundaries {
        private static final LocalDate UNLIMITED = null;
    }
}
