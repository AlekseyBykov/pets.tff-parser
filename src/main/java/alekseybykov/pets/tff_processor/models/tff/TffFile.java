package alekseybykov.pets.tff_processor.models.tff;

import alekseybykov.pets.tff_processor.exceptions.TffSchemeParseException;
import alekseybykov.pets.tff_processor.models.tff.data.TffDocument;
import alekseybykov.pets.tff_processor.models.tff.metadata.TffMetadata;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TffFile {

    private String version;
    private String marker;

    private List<TffMetadata> metadata = new ArrayList<>();
    private List<TffDocument> documents = new ArrayList<>();

    public TffMetadata findMetadataByMarker(String sectionMarker) {
        return metadata.stream()
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

    public List<TffMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<TffMetadata> metadata) {
        this.metadata = metadata;
    }

    public List<TffDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<TffDocument> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TffFile tffFile = (TffFile) o;

        return Objects.equals(version, tffFile.version) &&
                Objects.equals(marker, tffFile.marker) &&
                Objects.equals(metadata, tffFile.metadata) &&
                Objects.equals(documents, tffFile.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                marker,
                metadata,
                documents
        );
    }
}
