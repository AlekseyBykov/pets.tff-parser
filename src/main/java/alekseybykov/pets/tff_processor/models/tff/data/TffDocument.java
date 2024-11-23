package alekseybykov.pets.tff_processor.models.tff.data;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffDocument {

    private TffHeader header;
    private List<TffTable> tables = new ArrayList<>();

    public TffDocument() {
    }

    public TffDocument(TffHeader header) {
        this.header = header;
    }

    public List<TffTable> findTablesByMarker(String marker) {
        return tables.stream()
                .filter(table -> StringUtils.equals(table.getMarker(), marker))
                .toList();
    }

    public TffHeader getHeader() {
        return header;
    }

    public void setHeader(TffHeader header) {
        this.header = header;
    }

    public List<TffTable> getTables() {
        return tables;
    }

    public void setTables(List<TffTable> tables) {
        this.tables = tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffDocument document = (TffDocument) o;

        return Objects.equals(header, document.header) &&
                Objects.equals(tables, document.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                header,
                tables
        );
    }
}
