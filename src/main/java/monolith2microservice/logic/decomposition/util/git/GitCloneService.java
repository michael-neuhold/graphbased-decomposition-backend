package monolith2microservice.logic.decomposition.util.git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import monolith2microservice.Configs;
import monolith2microservice.shared.models.git.GitRepository;

@Service
public class GitCloneService {

	private static final Logger logger = LoggerFactory.getLogger(GitCloneService.class);
	
	private final String FILESYSTEM_DELIMITER = "/";
	private final String ID_NAME_DELIMITER = "_";

	@Autowired
	private Configs config;
	
    public void processRepository(GitRepository repo) throws Exception{
    	logger.info("Cloning repository "+ repo.getRemotePath() +" ...");
    	final String localRepoPath = config.localRepositoryDirectory + FILESYSTEM_DELIMITER + repo.getName() + ID_NAME_DELIMITER + repo.getId();
    	
    	// clone the repository from the remote location to the local filesystem
    	Git git = Git.cloneRepository().setURI(repo.getRemotePath()).setDirectory(new File(localRepoPath)).call();
    	git.close();
    	
    	logger.info("Cloned.");
    }
    
}
