package alekseybykov.pets.tff_processor.models.scheme;

import alekseybykov.pets.tff_processor.exceptions.TffSchemeParseException;
import alekseybykov.pets.tff_processor.models.scheme.data.TffSchemeDocument;
import alekseybykov.pets.tff_processor.models.scheme.metadata.TffSchemeMetadata;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffSchemeFile {

    private String version;
    private String marker;

    private List<TffSchemeMetadata> metadataSchemes = new ArrayList<>();
    private TffSchemeDocument data;

    public TffSchemeMetadata findMetadataSectionByMarker(String sectionMarker) {
        return metadataSchemes.stream()
                .filter(scheme -> StringUtils.equals(scheme.getMarker(), sectionMarker))
                .findFirst()
                .orElseThrow(
                        () -> TffSchemeParseException.missingSection(sectionMarker)
                );
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<TffSchemeMetadata> getMetadataSchemes() {
        return metadataSchemes;
    }

    public void setMetadataSchemes(List<TffSchemeMetadata> metadataSchemes) {
        this.metadataSchemes = metadataSchemes;
    }

    public TffSchemeDocument getData() {
        return data;
    }

    public void setData(TffSchemeDocument data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffSchemeFile that = (TffSchemeFile) o;

        return Objects.equals(version, that.version) &&
                Objects.equals(marker, that.marker) &&
                Objects.equals(metadataSchemes, that.metadataSchemes) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                marker,
                metadataSchemes,
                data
        );
    }
}
