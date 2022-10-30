package monolith2microservice.logic.decomposition.graph;

import monolith2microservice.logic.decomposition.engine.impl.shared.CouplingInput;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.couplings.*;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;

import java.util.*;

/**
 * Created by gmazlami on 12/15/16.
 */
public class LinearGraphCombination {

    private GitRepository gitRepository;

    private DecompositionParameters decompositionParameters;

    private List<ChangeEvent> history;

    public LinearGraphCombination(GitRepository gitRepository, List<ChangeEvent> history, DecompositionParameters decompositionParameters) {
        this.gitRepository = gitRepository;
        this.history = history;
        this.decompositionParameters = decompositionParameters;
    }

    private List<LogicalCoupling> logicalCouplings;

    private List<SemanticCoupling> semanticCouplings;

    private  List<ContributorCoupling> contributorCouplings;

    private int logicalCouplingFactor = 1;

    private int semanticCouplingFactor = 1;

    private int contributorCouplingFactor = 1;


    public static LinearGraphCombination create(GitRepository gitRepository, List<ChangeEvent> history, DecompositionParameters decompositionParameters) {
        return new LinearGraphCombination(gitRepository, history, decompositionParameters);
    }

    public LinearGraphCombination withLogicalCoupling(boolean include, CouplingEngine<LogicalCoupling> couplingEngine) {
        CouplingInput couplingInput = CouplingInput.builder()
                .decompositionParameters(decompositionParameters)
                .history(history)
                .gitRepository(gitRepository)
                .build();
        this.logicalCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withSemanticCoupling(boolean include, CouplingEngine<SemanticCoupling> couplingEngine) {
        CouplingInput couplingInput = CouplingInput.builder()
                .decompositionParameters(decompositionParameters)
                .history(history)
                .gitRepository(gitRepository)
                .build();
        this.semanticCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withContributorCoupling(boolean include, CouplingEngine<ContributorCoupling> couplingEngine) {
        CouplingInput couplingInput = CouplingInput.builder()
                .decompositionParameters(decompositionParameters)
                .history(history)
                .gitRepository(gitRepository)
                .build();
        this.contributorCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withLogicalFactor(int factor){
        this.logicalCouplingFactor = factor;
        return this;
    }

    public LinearGraphCombination withSemanticFactor(int factor){
        this.semanticCouplingFactor = factor;
        return this;
    }

    public LinearGraphCombination withContributorFactor(int factor){
        this.contributorCouplingFactor = factor;
        return this;
    }

    public List<BaseCoupling> generate(){
        List<CouplingTriple> triples = mapCouplingsOnSameEdge();
        List<BaseCoupling> couplings = new ArrayList<>();
        triples.forEach( t -> {
            double logicalWeight = t.getLogicalCoupling() == null ? 0 : this.logicalCouplingFactor * t.getLogicalCoupling().getScore();
            double semanticWeight = t.getSemanticCoupling() == null ? 0 : this.semanticCouplingFactor * t.getSemanticCoupling().getScore();
            double contributorWeight = t.getContributorCoupling() == null ? 0 : this.contributorCouplingFactor* t.getContributorCoupling().getScore();
            double combinedWeight = logicalWeight + semanticWeight + contributorWeight;
            BaseCoupling coupling = new BaseCoupling(t.getFirstFile(), t.getSecondFile(),combinedWeight);
            couplings.add(coupling);
        });
        return couplings;
    }



    private List<CouplingTriple> mapCouplingsOnSameEdge(){
        Map<String, CouplingTriple> couplingMap = new HashMap<>();

        if(this.logicalCouplings != null){
            this.logicalCouplings.forEach(l -> {
                CouplingTriple triple = new CouplingTriple();
                triple.setLogicalCoupling(l);
                triple.setFirstFile(l.getFirstFileName());
                triple.setSecondFile(l.getSecondFileName());
                couplingMap.put(generateKeyFromFileNames(l.getFirstFileName(), l.getSecondFileName()), triple);
            });
        }

        if(this.semanticCouplings != null){
            this.semanticCouplings.forEach(s -> {
                String key = generateKeyFromFileNames(s.getFirstFileName(), s.getSecondFileName());
                CouplingTriple triple = couplingMap.get(key);
                if(triple == null){
                    triple = new CouplingTriple();
                    triple.setSemanticCoupling(s);
                    triple.setFirstFile(s.getFirstFileName());
                    triple.setSecondFile(s.getSecondFileName());
                }else{
                    triple.setSemanticCoupling(s);
                }
                couplingMap.put(key, triple);
            });
        }

        if(this.contributorCouplings != null){
            this.contributorCouplings.forEach(c -> {
                String key = generateKeyFromFileNames(c.getFirstFileName(), c.getSecondFileName());
                CouplingTriple triple = couplingMap.get(key);
                if(triple == null){
                    triple = new CouplingTriple();
                    triple.setContributorCoupling(c);
                    triple.setFirstFile(c.getFirstFileName());
                    triple.setSecondFile(c.getSecondFileName());
                }else{
                    triple.setContributorCoupling(c);
                }
                couplingMap.put(key, triple);
            });
        }

        return new ArrayList<>(couplingMap.values());
    }

    private String generateKeyFromFileNames(String firstFileName, String secondFileName){
        List<String> fileNames = new ArrayList<>();
        fileNames.add(firstFileName);
        fileNames.add(secondFileName);
        Collections.sort(fileNames);
        return String.join("|",fileNames);
    }

}
