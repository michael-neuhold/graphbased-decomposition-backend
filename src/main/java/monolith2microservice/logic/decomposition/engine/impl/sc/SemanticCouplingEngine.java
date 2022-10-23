package monolith2microservice.logic.decomposition.engine.impl.sc;

import ch.uzh.ifi.seal.monolith2microservices.main.Configs;
import ch.uzh.ifi.seal.monolith2microservices.models.ClassContent;
import ch.uzh.ifi.seal.monolith2microservices.models.DecompositionParameters;
import ch.uzh.ifi.seal.monolith2microservices.models.couplings.SemanticCoupling;
import ch.uzh.ifi.seal.monolith2microservices.models.git.ChangeEvent;
import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;
import monolith2microservice.logic.decomposition.engine.impl.sc.classprocessing.ClassContentVisitor;
import monolith2microservice.logic.decomposition.engine.impl.sc.tfidf.TfIdfWrapper;
import ch.uzh.ifi.seal.monolith2microservices.utils.ClassContentFilter;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class SemanticCouplingEngine implements CouplingEngine<SemanticCoupling> {

    @Autowired
    private Configs config;

    @Override
    public List<SemanticCoupling> compute(GitRepository gitRepository, List<ChangeEvent> history, DecompositionParameters decompositionParameters) {
        List<SemanticCoupling> couplings = new ArrayList<>();

        //Read class files (content) from repo
        String localRepoPath = config.localRepositoryDirectory + "/" + gitRepository.getName() + "_" + gitRepository.getId();

        Path repoDirectory = Paths.get(localRepoPath);

        ClassContentVisitor visitor = new ClassContentVisitor(gitRepository,config, new ClassContentFilter());

        try {
            Files.walkFileTree(repoDirectory, visitor);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        List<ClassContent> classes = visitor.getClasses();

        for(ClassContent current: classes){
            for(ClassContent other: classes){
                if (!current.getFilePath().equals(other.getFilePath())) {
                    SemanticCoupling coupling = new SemanticCoupling(current.getFilePath(),other.getFilePath(), TfIdfWrapper.computeSimilarity(current.getTokenizedContent(), other.getTokenizedContent()));
                    couplings.add(coupling);
                }
            }
        }

        return couplings;
    }

}
