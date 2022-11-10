package monolith2microservice.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathBuilder {

    public static Path buildLocalRepoPath(String localRepositoryDirectory, String repositoryName, long repositoryId) {
        String path = String.format("%s/%s_%s", localRepositoryDirectory, repositoryName, repositoryId);
        return Paths.get(path);
    }

}
