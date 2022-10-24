package monolith2microservice.logic.decomposition.engine.impl.lc;

import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.couplings.LogicalCoupling;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import ch.uzh.ifi.seal.monolith2microservices.utils.Hash;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LogicalCouplingEngine implements CouplingEngine<LogicalCoupling> {

    private final String SUBSET_DELIMITER = "\\?";

    private Map<String, LogicalCoupling> resultMap;

    private int currentChangeEventCounter;

    @Override
    public List<LogicalCoupling> compute(GitRepository gitRepository, List<ChangeEvent> history, DecompositionParameters decompositionParameters) {
        resultMap = new HashMap<>();

        //compute first timestamp in history
        int t_start = extractEarliestTimestamp(history);
        int t_end = extractLatestTimestamp(history);
        Collections.reverse(history);

        for(int t_current = t_start; t_current < t_end; t_current+= decompositionParameters.getIntervalSeconds()){
            //Extract changeEvents that happened in this current time interval [t_current, t_current + intervalInSeconds]
            List<ChangeEvent> changeEvents = extractChangeEvents(history, t_current, t_current + decompositionParameters.getIntervalSeconds());

            if(changeEvents.isEmpty()){
                continue;
            }

            ImmutableSet<String> set = ImmutableSet.copyOf(getChangedFileNames(changeEvents));
            Set<Set<String>> powerSetofFileNames = Sets.powerSet(set);


            // for each pair of coupled files (A,B) in the powerset, create a logical coupling or increase score if coupling was already discovered
            for(Set<String> element: powerSetofFileNames){
                String[] files = new String[element.size()];
                files = element.toArray(files);

                //only consider subsets in the power set that have pair of 2 classes coupled together
                if (files.length == 2 ){
                    List<String> fileList = Arrays.asList(files);
                    LogicalCoupling coupling = generateLogicalCoupling(fileList, t_current, t_current + decompositionParameters.getIntervalSeconds());
                    resultMap.put(coupling.getHash(), coupling);
                }
            }
        }

        List<LogicalCoupling> couplings = new ArrayList<>(resultMap.values());
        currentChangeEventCounter = 0;
        resultMap = null;

        return couplings;
    }


    private int extractEarliestTimestamp(List<ChangeEvent> changeHistory){
        return changeHistory.get(changeHistory.size() - 1).getTimestampInSeconds();
    }

    private int extractLatestTimestamp(List<ChangeEvent> changeHistory){
        return changeHistory.get(0).getTimestampInSeconds();
    }

    private List<ChangeEvent> extractChangeEvents(List<ChangeEvent> changeHistory, int t_start, int t_end){
        List<ChangeEvent> results = new ArrayList<>();
        int time = t_start;

        while(currentChangeEventCounter < changeHistory.size() && time < t_end){
            ChangeEvent currentEvent = changeHistory.get(currentChangeEventCounter);

            if((t_start <= currentEvent.getTimestampInSeconds()) && (currentEvent.getTimestampInSeconds() < t_end)){
                results.add(currentEvent);
                currentChangeEventCounter++;
            }else{
                time = currentEvent.getTimestampInSeconds();
            }

        }
        return results;
    }


    private LogicalCoupling generateLogicalCoupling(List<String> fileList, int t_start, int t_end){

        Collections.sort(fileList);
        String key = String.join(SUBSET_DELIMITER, fileList);
        String hash = new Hash(key).get();

        LogicalCoupling existingCoupling = resultMap.get(hash);

        //check if this coupling was already discovered
        if(existingCoupling != null){
            //if so, we only have to increment the score
            existingCoupling.incrementScore();
            return existingCoupling;
        }else{
            // if the coupling is a new one (new file combination that changed together), we have to create it
            LogicalCoupling newCoupling = new LogicalCoupling(fileList.get(0),fileList.get(1),1);
            newCoupling.setHash(hash);
            newCoupling.setStartTimestamp(t_start);
            newCoupling.setEndTimestamp(t_end);

            return newCoupling;
        }
    }

    /*
     * Converts a list of DiffEntry instances to a list of the path strings
     * of the file changes in the DiffEntry instance.
     */
    private List<String> getStringSet(List<DiffEntry> diffList){
        List<String> set = new ArrayList<>();
        for(DiffEntry entry: diffList){
            set.add(entry.getNewPath());
        }
        return set;
    }

    private List<String> getChangedFileNames(List<ChangeEvent> events){
        List<String> fileNames = new ArrayList<>();
        for(ChangeEvent event: events){
            fileNames.addAll(event.getChangedFileNames());
        }
        if(fileNames.size() > 12){
            return fileNames.subList(0,11);
        }else{
            return  fileNames;
        }

    }

}
