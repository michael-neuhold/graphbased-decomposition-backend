package monolith2microservice.util;

import java.util.List;

/**
 * Created by Genc on 15.01.2017.
 */

@FunctionalInterface
public interface FilterInterface {

    List<String> filterFileContent(String rawFileConent);
}

