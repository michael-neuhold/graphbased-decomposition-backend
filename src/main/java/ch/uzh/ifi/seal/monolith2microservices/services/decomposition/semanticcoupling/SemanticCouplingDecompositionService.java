package ch.uzh.ifi.seal.monolith2microservices.services.decomposition.semanticcoupling;

import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;
import ch.uzh.ifi.seal.monolith2microservices.services.decomposition.Decompositor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by gmazlami on 12/6/16.
 */
@Service
public class SemanticCouplingDecompositionService {

    private static final Logger logger = LoggerFactory.getLogger(SemanticCouplingDecompositionService.class);

    @Autowired
    @Qualifier("semanticCouplingDecompositor")
    Decompositor decompositor;

    @Async
    public void process(GitRepository repo){
        long startTime = System.currentTimeMillis();

        decompositor.decompose(repo);

        long endTime = System.currentTimeMillis();

        logger.info("Total execution time: " + String.valueOf(endTime - startTime) + " ms");
    }
}
