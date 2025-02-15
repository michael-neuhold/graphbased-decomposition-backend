package monolith2microservice.util.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import monolith2microservice.logic.decomposition.util.git.FilterService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import monolith2microservice.Configs;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;

import static monolith2microservice.util.PathBuilder.buildLocalRepoPathString;

public class GitClient {

    private final Configs config;

    private final GitRepository repo;

    public GitClient(GitRepository repo, Configs config) {
        this.repo = repo;
        this.config = config;
    }

    public Repository getDotGitFolder() throws IOException {
        return new FileRepositoryBuilder()
                .setGitDir(new File(buildLocalRepoPathString(config.localRepositoryDirectory, repo.getName(), repo.getId())))
                .readEnvironment()
                .findGitDir()
                .build();
    }

    public List<RevCommit> getCommitLog() throws Exception {
        Git git = new Git(getDotGitFolder());
        Iterable<RevCommit> log = git.log().call();
        git.close();
        return ImmutableList.copyOf(log);
    }

    public List<ChangeEvent> getChangeEvents() throws Exception {
        List<ChangeEvent> changeHistory = new ArrayList<>();

        Repository repository = getDotGitFolder();
        Git git = new Git(repository);
        RevWalk walk = new RevWalk(repository);
        ObjectReader reader = repository.newObjectReader();
        List<RevCommit> log = getCommitLog();

        RevCommit first, second;
        RevTree firstTree, secondTree;
        CanonicalTreeParser firstTreeIter = new CanonicalTreeParser();
        CanonicalTreeParser secondTreeIter = new CanonicalTreeParser();
        ChangeEvent event;

        for (int i = 0; i < log.size() - 1; i++) {
            first = log.get(i);
            second = log.get(i + 1);

            firstTree = walk.parseTree(first.getTree().getId());
            secondTree = walk.parseTree(second.getTree().getId());

            firstTreeIter.reset(reader, firstTree);
            secondTreeIter.reset(reader, secondTree);

            List<DiffEntry> diffs = git.diff().setNewTree(firstTreeIter).setOldTree(secondTreeIter).call();
            diffs = FilterService.filterBlackList(diffs);
            event = new ChangeEvent(first.getCommitTime(), diffs, first);
            event.setAuthorEmailAddress(second.getAuthorIdent().getEmailAddress());
            changeHistory.add(event);

        }

        git.close();
        walk.close();
        return changeHistory.stream().filter(changeEvent -> changeEvent.getChangedfiles().size() > 0).collect(Collectors.toList());
    }

}
