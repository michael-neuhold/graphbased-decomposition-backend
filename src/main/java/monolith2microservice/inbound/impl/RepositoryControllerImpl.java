package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.RepositoryController;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.git.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("repositories")
public class RepositoryControllerImpl implements RepositoryController {

	private final Logger LOGGER = LoggerFactory.getLogger(RepositoryControllerImpl.class);

	@Autowired
	private RepositoryLogic repositoryLogic;

	@Override
	@CrossOrigin
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<GitRepository> addRepository(@RequestBody RepositoryDto repositoryDto) {
		LOGGER.info("|-> addRepository");
		GitRepository storedRepository =  repositoryLogic.add(repositoryDto);
		if (storedRepository == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.ok(storedRepository);
    }

	@Override
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<GitRepository>> getAllRepositories() {
		LOGGER.info("|-> getAllRepositories");
		return ResponseEntity.ok(repositoryLogic.findAll());
	}

	@Override
	@CrossOrigin
	@RequestMapping(value="{repositoryId}", method=RequestMethod.GET)
	public ResponseEntity<GitRepository> getRepositoryById(@PathVariable Long repositoryId) {
		LOGGER.info("|-> getRepositoryById");
		GitRepository gitRepository = repositoryLogic.findById(repositoryId);
		if (gitRepository == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(gitRepository);
	}
    
}
