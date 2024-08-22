package alekseybykov.pets.tff_processor.models.scheme.data;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffSchemeDocument {

    private TffSchemeHeader header;
    private List<TffSchemeTable> tables = new ArrayList<>();

    public TffSchemeTable findTableByMarker(String marker) {
        return tables.stream()
                .filter(table -> StringUtils.equals(table.getMarker(), marker))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public TffSchemeHeader getHeader() {
        return header;
    }

    public void setHeader(TffSchemeHeader header) {
        this.header = header;
    }

    public List<TffSchemeTable> getTables() {
        return tables;
    }

    public void setTables(List<TffSchemeTable> tables) {
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

        TffSchemeDocument that = (TffSchemeDocument) o;

        return Objects.equals(header, that.header) &&
                Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                header,
                tables
        );
    }
}
