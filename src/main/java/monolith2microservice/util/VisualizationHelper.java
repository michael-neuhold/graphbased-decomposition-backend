package monolith2microservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VisualizationHelper {

    private static final List<String> API_KEYWORDS = new ArrayList<>();

    private static final List<String> NON_API_KEYWORDS = new ArrayList<>();

    private static final List<String> DATABASE_ACCESS_KEYWORDS = new ArrayList<>();

    private static final List<String> NON_DATABASE_ACCESS_KEYWORDS = new ArrayList<>();

    static {
        API_KEYWORDS.add("API");
        API_KEYWORDS.add("ENDPOINT");
        API_KEYWORDS.add("RESOURCE");
        API_KEYWORDS.add("CONTROLLER");

        NON_API_KEYWORDS.add("MAPPER");
        NON_API_KEYWORDS.add("SERVICE");
        NON_API_KEYWORDS.add("LOGIC");
        NON_API_KEYWORDS.add("TEST");

        DATABASE_ACCESS_KEYWORDS.add("DAO");
        DATABASE_ACCESS_KEYWORDS.add("REPOSITORY");
        DATABASE_ACCESS_KEYWORDS.add("JDBC");

        NON_DATABASE_ACCESS_KEYWORDS.add("MAPPER");
        NON_DATABASE_ACCESS_KEYWORDS.add("SERVICE");
        NON_DATABASE_ACCESS_KEYWORDS.add("LOGIC");
        NON_DATABASE_ACCESS_KEYWORDS.add("TEST");
    }

    public static boolean couldBeApi(String label) {
        return doesContainKeyword(label, API_KEYWORDS) &&
                !doesContainKeyword(label, NON_API_KEYWORDS);
    }

    public static boolean couldBeDatabaseAccess(String label) {
        return doesContainKeyword(label, DATABASE_ACCESS_KEYWORDS) &&
                !doesContainKeyword(label, NON_DATABASE_ACCESS_KEYWORDS);
    }

    private static boolean doesContainKeyword(String label, List<String> keywords) {
        for (String keyword : keywords) {
            if (label.toUpperCase(Locale.ROOT).contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
