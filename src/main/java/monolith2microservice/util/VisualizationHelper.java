package monolith2microservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VisualizationHelper {

    private static final List<String> API_KEYWORDS = new ArrayList<>();
    private static final List<String> NON_API_KEYWORDS = new ArrayList<>();

    static {
        API_KEYWORDS.add("API");
        API_KEYWORDS.add("ENDPOINT");
        API_KEYWORDS.add("RESOURCE");
        API_KEYWORDS.add("CONTROLLER");

        NON_API_KEYWORDS.add("MAPPER");
        NON_API_KEYWORDS.add("SERVICE");
        NON_API_KEYWORDS.add("LOGIC");
        NON_API_KEYWORDS.add("TEST");
    }

    public static boolean couldBeApi(String label) {
        return doesContainApiKeyword(label) && !doesContainNonApiKeyWord(label);
    }

    private static boolean doesContainApiKeyword(String label) {
        for (String keyword : API_KEYWORDS) {
            if (label.toUpperCase(Locale.ROOT).contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesContainNonApiKeyWord(String label) {
        for (String keyword : NON_API_KEYWORDS) {
            if (label.toUpperCase(Locale.ROOT).contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
