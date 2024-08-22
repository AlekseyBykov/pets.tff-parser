package alekseybykov.pets.tff_processor.locators;

import java.time.LocalDate;

public interface ITffSchemeLocator {

    String locateActualScheme(String documentMarker, LocalDate date);
}
