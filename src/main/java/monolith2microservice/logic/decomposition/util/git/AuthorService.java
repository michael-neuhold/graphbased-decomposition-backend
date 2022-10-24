package monolith2microservice.logic.decomposition.util.git;

import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gmazlami on 1/12/17.
 */
@Service
public class AuthorService {

    @Autowired
    private HistoryService historyService;

    public Set<String> getContributingAuthors(GitRepository gitRepo, String filePath) throws Exception{
        List<ChangeEvent> history = historyService.computeChangeEvents(gitRepo);
        Set<String> contributingAuthors = new HashSet<>();
        history.forEach(event -> {
            boolean matches = event.getChangedfiles().stream().anyMatch(changedFile -> changedFile.getNewPath().equals(filePath));
            if(matches){
                contributingAuthors.add(event.getAuthorEmailAddress());
            }
        });
        return contributingAuthors;
    }
}
