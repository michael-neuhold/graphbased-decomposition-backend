package monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor;

import monolith2microservice.Configs;
import monolith2microservice.logic.decomposition.engine.impl.shared.BaseClassVisitor;
import monolith2microservice.logic.decomposition.engine.impl.shared.ClassVisitor;
import monolith2microservice.logic.decomposition.engine.impl.sc.classvisitor.StopWords;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.util.FilterInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DependencyCouplingClassVisitor extends BaseClassVisitor implements ClassVisitor<List<DependencyCouplingClassVisitorResult>> {

    private FilterInterface filterInterface;

    private DependencyCouplingClassVisitor(GitRepository gitRepository, Configs configs) {
        super(gitRepository, configs);
    }

    private final List<DependencyCouplingClassVisitorResult> results = new ArrayList<>();

    public static DependencyCouplingClassVisitor createWith(GitRepository gitRepository, Configs configs) {
        return new DependencyCouplingClassVisitor(gitRepository, configs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if(allowedFilesMatcher.matches(file.getFileName())) {
            processFileContent(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public List<DependencyCouplingClassVisitorResult> getResult() {
        return results;
    }

    private void processFileContent(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)){
            StringBuilder sb = new StringBuilder();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (StopWords.IMPORT_KEYWORDS.stream().anyMatch(currentLine::startsWith)) {
                    sb.append(currentLine);
                }
            }
            DependencyCouplingClassVisitorResult dependencyCouplingClassVisitorResult = DependencyCouplingClassVisitorResult.builder()
                    .filePath(getRelativeFileName(file.toUri().toString()))
                    .tokenizedDependecies(filterInterface.filterFileContent(sb.toString()))
                    .build();
            this.results.add(dependencyCouplingClassVisitorResult);
        } catch (IOException mE) {
            throw new RuntimeException(mE);
        }
    }

}
