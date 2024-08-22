package alekseybykov.pets.tff_processor.locators.impl;

import alekseybykov.pets.tff_processor.exceptions.TffSchemeNotFoundException;
import alekseybykov.pets.tff_processor.locators.ITffSchemeLocator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TffSchemeLocator implements ITffSchemeLocator {

    @Override
    public String locateActualScheme(
            String documentMarker,
            LocalDate date
    ) {
        // todo поиск имени файла актуальной схемы в БД
        if (StringUtils.equals(documentMarker, "UF")) {
            return "TXUF180101.xml";
        }

        throw new TffSchemeNotFoundException(
                String.format(
                        "Не найдена актуальная на дату %s ТФФ схема для маркера %s",
                        date,
                        documentMarker
                )
        );
    }
}
