package monolith2microservice.logic.decomposition.engine.impl.sc.classprocessing;

import java.util.ArrayList;
import java.util.List;

public class StopWordFilter {

	public List<String> filter(String rawFileContent){

        List<String> filteredContent = new ArrayList<>();

        String[] tokens = rawFileContent.split("\\s+");

        for(String token: tokens){
            if(!StopWords.JAVA_KEYWORDS.contains(token)){
                filteredContent.add(token);
            }
        }

        return filteredContent;
    }
	
	
}
