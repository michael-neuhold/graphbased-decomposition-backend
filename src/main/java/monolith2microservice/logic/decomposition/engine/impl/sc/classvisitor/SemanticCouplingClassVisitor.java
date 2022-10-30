package monolith2microservice.logic.decomposition.engine.impl.sc.classvisitor;

import monolith2microservice.Configs;
import monolith2microservice.logic.decomposition.engine.impl.shared.classvisitor.BaseClassVisitor;
import monolith2microservice.logic.decomposition.engine.impl.shared.classvisitor.ClassVisitor;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.util.FilterInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SemanticCouplingClassVisitor extends BaseClassVisitor implements ClassVisitor<List<SemanticCouplingClassVisitorResult>> {

    private final List<SemanticCouplingClassVisitorResult> results = new ArrayList<>();

    private final FilterInterface filterInterface;

    public SemanticCouplingClassVisitor(GitRepository repo, Configs config, FilterInterface filterInterface) {
        super(repo, config);
        this.filterInterface = filterInterface;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        if (allowedFilesMatcher.matches(path.getFileName())){
            processFileContent(path);
        }
        return FileVisitResult.CONTINUE;
    }

    private void processFileContent(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            StringBuilder sb = new StringBuilder();
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                if(StopWords.IMPORT_KEYWORDS.stream().anyMatch(currentLine::startsWith)){
                    continue;
                }
                sb.append(currentLine);
            }

            SemanticCouplingClassVisitorResult semanticCouplingClassVisitorResult = SemanticCouplingClassVisitorResult.builder()
                    .filePath(getRelativeFileName(path.toUri().toString()))
                    .tokenizedContent(filterInterface.filterFileContent(sb.toString()))
                    .build();
            this.results.add(semanticCouplingClassVisitorResult);
        } catch (IOException mE) {
            throw new RuntimeException(mE);
        }
    }

    @Override
    public List<SemanticCouplingClassVisitorResult> getResult() {
       return results;
    }
}
