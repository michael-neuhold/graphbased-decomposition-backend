package monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class DependencyCouplingClassVisitor extends SimpleFileVisitor<Path> implements ClassVisitor<List<DependencyCouplingClassVisitorResult>> {

    private DependencyCouplingClassVisitor() {
        // nothing to do
    }

    public static DependencyCouplingClassVisitor create() {
        return new DependencyCouplingClassVisitor();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return super.visitFile(file, attrs);
    }

    @Override
    public List<DependencyCouplingClassVisitorResult> getResult() {
        return null;
    }

}
