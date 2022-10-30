package monolith2microservice.logic.decomposition.engine.impl.shared.classvisitor;

import monolith2microservice.Configs;
import monolith2microservice.shared.models.git.GitRepository;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;

public abstract class BaseClassVisitor extends SimpleFileVisitor<Path> {

    protected final PathMatcher allowedFilesMatcher = FileSystems.getDefault().getPathMatcher("glob:*.{java,py,rb}");

    protected GitRepository gitRepository;

    protected Configs config;

    public BaseClassVisitor(GitRepository gitRepository, Configs config) {
        this.gitRepository = gitRepository;
        this.config = config;
    }

    protected String getRelativeFileName(String filePath){
        String[] packageNameArray = filePath.split(config.localRepositoryDirectory);
        String qualifiedPathName;
        if(packageNameArray.length > 2){
            qualifiedPathName = filePath.replace(packageNameArray[0]+config.localRepositoryDirectory, "");
        }else{
            qualifiedPathName = packageNameArray[1];
        }
        return qualifiedPathName.replace(this.gitRepository.getDirectoryName(),"").substring(2);
    }

}
