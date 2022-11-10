package monolith2microservice.logic.evaluation.impl.evaluators;

import monolith2microservice.Configs;
import monolith2microservice.util.PathBuilder;
import monolith2microservice.shared.models.evaluation.MicroserviceMetrics;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.ClassNode;
import monolith2microservice.shared.models.graph.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class ServiceEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEvaluator.class);

    @Autowired
    private Configs config;

    public MicroserviceMetrics computeMetrics(Component microservice, GitRepository repo, List<ChangeEvent> history) {
        MicroserviceMetrics metrics = new MicroserviceMetrics(microservice);
        metrics.setContributors(computeAuthorSet(microservice, generateAuthorMap(history)));
        metrics.setLOC(computeSizeInLinesOfCode(microservice, repo));
        return metrics;
    }

    private Map<String, Set<String>> generateAuthorMap(List<ChangeEvent> history) {
        Map<String, Set<String>> fileAuthorMap = new HashMap<>();

        for (ChangeEvent event : history) {
            for (String fileName : event.getChangedFileNames()) {
                if (fileAuthorMap.get(fileName) == null) {
                    Set<String> authorSet = new HashSet<>();
                    authorSet.add(event.getAuthorEmailAddress());
                    fileAuthorMap.put(fileName, authorSet);
                } else {
                    Set<String> authorSet = fileAuthorMap.get(fileName);
                    authorSet.add(event.getAuthorEmailAddress());
                    fileAuthorMap.put(fileName, authorSet);
                }
            }
        }

        return fileAuthorMap;
    }

    private Set<String> computeAuthorSet(Component microservice, Map<String, Set<String>> authorMap) {
        Set<String> authorSet = new HashSet<>();

        for (ClassNode node : microservice.getNodes()) {
            Set<String> authors = authorMap.get(node.getId());
            if (authors != null) {
                authorSet.addAll(authors);
            }
        }

        return authorSet;
    }

    private int computeSizeInLinesOfCode(Component microservice, GitRepository repo) {
        List<String> filePaths = microservice.getNodes().stream()
                .map(ClassNode::getId)
                .collect(Collectors.toList());

        Path repositoryDirectory = PathBuilder.buildLocalRepoPath(
                config.localRepositoryDirectory,
                repo.getName(),
                repo.getId()
        );

        int lineCounter = 0;
        for (String filePath : filePaths) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(repositoryDirectory + "/" + filePath))) {
                while (reader.readLine() != null) {
                    lineCounter++;
                }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        return lineCounter;
    }

}
