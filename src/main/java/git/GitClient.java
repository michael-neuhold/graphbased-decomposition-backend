package git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import main.utils.Configs;
import models.ChangeEvent;
import models.GitRepository;

public class GitClient {

	private Configs config;
	
	private GitRepository repo;
	
	public GitClient(GitRepository repo, Configs config){
		this.repo = repo;
		this.config = config;
	}
	
	public void cloneRepository() throws Exception {
    	Git git = Git.cloneRepository().setURI(repo.getRemotePath()).setDirectory(new File(config.localRepositoryDirectory + "/" + repo.getName() + "_" + repo.getId())).call();
    	git.close();
	}
	
	public Repository getGitRepository() throws IOException {
		File file = new File(config.localRepositoryDirectory + "/" + repo.getName()+"_"+repo.getId()+"/.git");
		return new FileRepositoryBuilder().setGitDir(file).readEnvironment().findGitDir().build();
	}
	
	public List<RevCommit> getCommitLog() throws Exception{
		Git git = new Git(getGitRepository());
		Iterable<RevCommit> log = git.log().call(); 
		git.close();
		List<RevCommit> logList = new ArrayList<>();
		log.forEach(logList::add);
		return logList;
	}
	
	public RepositoryHistory getChangeHistory() throws Exception{
		Repository repository = getGitRepository();
		Git git = new Git(repository);
		ObjectReader reader = repository.newObjectReader();
		RevWalk walk = new RevWalk(repository);
		List<ChangeEvent> changeHistory = new ArrayList<>();		
		
		List<RevCommit> log = getCommitLog();
		
		Map<ObjectId,List<DiffEntry>> diffHistory = new HashMap<>();
		
		
		RevCommit first, second;
		RevTree firstTree, secondTree;
		CanonicalTreeParser firstTreeIter = new CanonicalTreeParser();
		CanonicalTreeParser secondTreeIter = new CanonicalTreeParser();
		ChangeEvent event;
		
		for(int i=0; i < log.size() - 1; i++){
			first = log.get(i);
			second = log.get(i+1);
			
			firstTree = walk.parseTree(first.getTree().getId());
			secondTree = walk.parseTree(second.getTree().getId());
			
    		firstTreeIter.reset(reader, firstTree);
    		secondTreeIter.reset(reader, secondTree);
    		
            List<DiffEntry> diffs = git.diff().setNewTree(firstTreeIter).setOldTree(secondTreeIter).call();
            diffHistory.put(first, diffs);
            
            event = new ChangeEvent(first.getCommitTime(),diffs,first);
            changeHistory.add(event);
            
		}
		
		log.remove(0);
		git.close();
		walk.close();
		return new RepositoryHistory(log, diffHistory, changeHistory);
	}
	
	
}
