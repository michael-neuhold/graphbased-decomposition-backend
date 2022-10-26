package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.RepositoryController;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("repositories")
public class RepositoryControllerImpl implements RepositoryController {

	@Autowired
	private RepositoryLogic repositoryLogic;

	@Override
	@CrossOrigin
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<GitRepository> addRepository(@RequestBody RepositoryDto repositoryDto) {
		GitRepository storedRepository = null;
		try {
			storedRepository = repositoryLogic.add(repositoryDto);
		} catch (Exception exception) {
			// TODO
		}
		return ResponseEntity.ok(storedRepository);
    }

	@Override
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<GitRepository>> getAllRepositories() {
		return ResponseEntity.ok(repositoryLogic.findAll());
	}

	@Override
	@CrossOrigin
	@RequestMapping(value="{repositoryId}", method=RequestMethod.GET)
	public ResponseEntity<GitRepository> getRepositoryById(@PathVariable Long repositoryId) {
		return ResponseEntity.ok(repositoryLogic.findById(repositoryId));
	}
    
}
