package monolith2microservice.logic.repository.impl;

import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.outbound.RepositoryRepository;
import monolith2microservice.logic.decomposition.util.git.GitCloneService;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryLogicImpl implements RepositoryLogic {

    private final Logger logger = LoggerFactory.getLogger(RepositoryLogicImpl.class);

    @Autowired
    private RepositoryRepository repository;

    @Autowired
    private GitCloneService gitCloneService;

    @Override
    public List<GitRepository> findAll() {
        logger.info("findAll");
        return repository.findAll();
    }

    @Override
    public GitRepository findById(Long repositoryId) {
        logger.info("findById");
        return repository.findById(repositoryId);
    }

    @Override
    public GitRepository add(RepositoryDto repositoryDto) {
        logger.info("add");

        GitRepository r = repository.save(new GitRepository(repositoryDto.getUri(), repositoryDto.getName()));
        try {
            gitCloneService.processRepository(r);
        } catch (Exception exception) {
            return null;
        }

        return r;
    }

}
