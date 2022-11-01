package monolith2microservice.logic.decomposition.graph;

import monolith2microservice.logic.decomposition.engine.impl.shared.CouplingInput;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.couplings.*;
import monolith2microservice.shared.models.couplings.impl.ContributorCoupling;
import monolith2microservice.shared.models.couplings.impl.DependencyCoupling;
import monolith2microservice.shared.models.couplings.impl.LogicalCoupling;
import monolith2microservice.shared.models.couplings.impl.SemanticCoupling;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;

import java.util.*;

public class LinearGraphCombination {

    public LinearGraphCombination(GitRepository gitRepository, List<ChangeEvent> history,
                                  DecompositionCouplingParameters decompositionParameters) {
        this.couplingInput = CouplingInput.builder()
                .decompositionParameters(decompositionParameters)
                .history(history)
                .gitRepository(gitRepository)
                .build();
    }

    private final CouplingInput couplingInput;

    private List<LogicalCoupling> logicalCouplings;

    private List<SemanticCoupling> semanticCouplings;

    private  List<ContributorCoupling> contributorCouplings;

    private List<DependencyCoupling> dependencyCouplings;

    private int logicalCouplingFactor = 1;

    private int semanticCouplingFactor = 1;

    private int contributorCouplingFactor = 1;

    private int dependencyCouplingFactor = 1;


    public static LinearGraphCombination createWith(GitRepository gitRepository, List<ChangeEvent> history,
                                                    DecompositionCouplingParameters decompositionParameters) {
        return new LinearGraphCombination(gitRepository, history, decompositionParameters);
    }

    public LinearGraphCombination withLogicalCoupling(boolean include, CouplingEngine<LogicalCoupling> couplingEngine) {
        this.logicalCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withSemanticCoupling(boolean include, CouplingEngine<SemanticCoupling> couplingEngine) {
        this.semanticCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withContributorCoupling(boolean include, CouplingEngine<ContributorCoupling> couplingEngine) {
        this.contributorCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withDependencyCoupling(boolean include, CouplingEngine<DependencyCoupling> couplingEngine) {
        this.dependencyCouplings = include ? couplingEngine.compute(couplingInput) : null;
        return this;
    }

    public LinearGraphCombination withLogicalFactor(int factor) {
        this.logicalCouplingFactor = factor;
        return this;
    }

    public LinearGraphCombination withSemanticFactor(int factor) {
        this.semanticCouplingFactor = factor;
        return this;
    }

    public LinearGraphCombination withContributorFactor(int factor) {
        this.contributorCouplingFactor = factor;
        return this;
    }

    public LinearGraphCombination withDependencyFactor(int factor) {
        this.dependencyCouplingFactor = factor;
        return this;
    }

    public List<BaseCoupling> generate() {
        List<CouplingQuartett> quartetts = mapCouplingsOnSameEdge();
        List<BaseCoupling> couplings = new ArrayList<>();
        quartetts.forEach( quartett -> {
            double logicalWeight = quartett.getLogicalCoupling() == null ? 0 : this.logicalCouplingFactor * quartett.getLogicalCoupling().getScore();
            double semanticWeight = quartett.getSemanticCoupling() == null ? 0 : this.semanticCouplingFactor * quartett.getSemanticCoupling().getScore();
            double contributorWeight = quartett.getContributorCoupling() == null ? 0 : this.contributorCouplingFactor * quartett.getContributorCoupling().getScore();
            double dependencyWeight = quartett.getDependencyCoupling() == null ? 0 : this.dependencyCouplingFactor * quartett.getDependencyCoupling().getScore();
            double combinedWeight = logicalWeight + semanticWeight + contributorWeight + dependencyWeight;
            BaseCoupling coupling = new BaseCoupling(quartett.getFirstFile(), quartett.getSecondFile(), combinedWeight);
            couplings.add(coupling);
        });
        return couplings;
    }

    private List<CouplingQuartett> mapCouplingsOnSameEdge() {
        Map<String, CouplingQuartett> couplingMap = new HashMap<>();

        if (this.logicalCouplings != null) {
            this.logicalCouplings.forEach(logicalCoupling -> {
                String key = generateKeyFromFileNames(logicalCoupling.getFirstFileName(), logicalCoupling.getSecondFileName());
                CouplingQuartett quartett = new CouplingQuartett(logicalCoupling);
                couplingMap.put(
                        key,
                        quartett
                );
            });
        }

        if (this.semanticCouplings != null) {
            this.semanticCouplings.forEach(semanticCoupling -> {
                String key = generateKeyFromFileNames(semanticCoupling.getFirstFileName(), semanticCoupling.getSecondFileName());
                CouplingQuartett quartett = couplingMap.get(key);
                if (quartett == null) {
                    quartett = new CouplingQuartett(semanticCoupling);
                } else {
                    quartett.setSemanticCoupling(semanticCoupling);
                }
                couplingMap.put(key, quartett);
            });
        }

        if (this.contributorCouplings != null) {
            this.contributorCouplings.forEach(contributorCoupling -> {
                String key = generateKeyFromFileNames(contributorCoupling.getFirstFileName(), contributorCoupling.getSecondFileName());
                CouplingQuartett quartett = couplingMap.get(key);
                if (quartett == null) {
                    quartett = new CouplingQuartett(contributorCoupling);
                } else {
                    quartett.setContributorCoupling(contributorCoupling);
                }
                couplingMap.put(key, quartett);
            });
        }

        if (this.dependencyCouplings != null) {
            this.dependencyCouplings.forEach(dependencyCoupling -> {
                String key = generateKeyFromFileNames(dependencyCoupling.getFirstFileName(), dependencyCoupling.getSecondFileName());
                CouplingQuartett quartett = couplingMap.get(key);
                if (quartett == null) {
                    quartett = new CouplingQuartett(dependencyCoupling);
                } else {
                    quartett.setDependencyCoupling(dependencyCoupling);
                }
                couplingMap.put(key, quartett);
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
