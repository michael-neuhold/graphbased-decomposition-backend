package monolith2microservice.logic.decomposition.engine.impl.cc;


import monolith2microservice.logic.decomposition.engine.impl.shared.CouplingInput;
import monolith2microservice.shared.models.couplings.impl.ContributorCoupling;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContributorCouplingEngine implements CouplingEngine<ContributorCoupling> {

    @Override
    public List<ContributorCoupling> compute(CouplingInput couplingInput) {
        Map<String, List<String>> fileAuthorMap = getFileAuthorMap(couplingInput.getHistory());
        List<String> fileNames = new ArrayList<>(fileAuthorMap.keySet());

        List<ContributorCoupling> couplings = new ArrayList<>();

        for(int i = 0; i < fileNames.size(); i++){
            String currentFileName = fileNames.get(i);

            List<String> currentFileAuthors = fileAuthorMap.get(currentFileName);

            for(String secondFileName : fileNames.subList(i+1, fileNames.size())){
                List<String> secondFileAuthors = fileAuthorMap.get(secondFileName);
                int similarity = computeAuthorSimilarity(currentFileAuthors, secondFileAuthors);

                ContributorCoupling coupling = ContributorCoupling.of(currentFileName, secondFileName, similarity);
                coupling.setFirstFileAuthors(currentFileAuthors);
                coupling.setSecondFileAuthors(secondFileAuthors);
                couplings.add(coupling);
            }

        }
        return couplings;
    }

    private Map<String, List<String>> getFileAuthorMap(List<ChangeEvent> changeHistory){
        Map<String,List<String>> fileAuthorMap = new HashMap<>();

        for(ChangeEvent event : changeHistory){
            for(String fileName: event.getChangedFileNames()){
                if(fileAuthorMap.get(fileName) == null){
                    List<String> list = new ArrayList<>();
                    list.add(event.getAuthorEmailAddress());
                    fileAuthorMap.put(fileName, list);

                }else{
                    List<String> list = fileAuthorMap.get(fileName);
                    list.add(event.getAuthorEmailAddress());
                    fileAuthorMap.put(fileName, list);
                }
            }
        }

        return fileAuthorMap;
    }

    private int computeAuthorSimilarity(List<String> firstFileAuthors, List<String> secondFileAuthors){
        Map<String,?> map = new HashMap<>();
        int numberOfSharedAuthors = 0;

        for(String author: firstFileAuthors){
            map.put(author, null);
        }

        for(String otherAuthor : secondFileAuthors){
            if(map.containsKey(otherAuthor)){
                numberOfSharedAuthors++;
            }
        }

        return numberOfSharedAuthors;
    }

}
