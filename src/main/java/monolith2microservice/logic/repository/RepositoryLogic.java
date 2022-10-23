package monolith2microservice.logic.repository;

import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;
import monolith2microservice.shared.dto.RepositoryDto;

import java.util.List;

public interface RepositoryLogic {

    List<GitRepository> findAll();
    GitRepository findById(Long repositoryId);
    GitRepository add(RepositoryDto repositoryDto) throws Exception;

}
