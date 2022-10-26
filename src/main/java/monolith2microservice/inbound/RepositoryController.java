package monolith2microservice.inbound;

import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RepositoryController {
    ResponseEntity<GitRepository> addRepository(RepositoryDto repositoryDto);

    ResponseEntity<List<GitRepository>> getAllRepositories();

    ResponseEntity<GitRepository> getRepositoryById(Long repositoryId);

}
