package monolith2microservice.logic.decomposition.util.git;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import monolith2microservice.util.git.ClassVisitor;
import monolith2microservice.Configs;
import monolith2microservice.shared.models.git.Class;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.outbound.ClassRepository;

@Service
public class ParsingService {

	@Autowired
	private Configs config;
	
	@Autowired
	private ClassRepository classRepository;
	
	@Async
	public void parseClasses(GitRepository repo) throws Exception{
		String localRepoPath = config.localRepositoryDirectory + "/" + repo.getName() + "_" + repo.getId();
		
		Path repoDirectory = Paths.get(localRepoPath);
		
		ClassVisitor visitor = new ClassVisitor(repo, config);
		Files.walkFileTree(repoDirectory, visitor);
		
		List<Class> classes = visitor.getClasses();
		
		classes.forEach(cls -> classRepository.save(cls));
		
	}
}
