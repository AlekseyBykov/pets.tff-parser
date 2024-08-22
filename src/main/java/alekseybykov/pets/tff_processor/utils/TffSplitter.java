package alekseybykov.pets.tff_processor.utils;

import org.apache.commons.lang3.StringUtils;

public class TffSplitter {

    private static final String DELIMITER = "\\|";
    private static final String SECTION_EOL = "\\|$";

    private static final int UNLIMITED_FLAG = -1;

    public static String[] split(String line) {
        return line
                .replaceAll(SECTION_EOL, StringUtils.EMPTY)
                .split(DELIMITER, UNLIMITED_FLAG);
    }
}
