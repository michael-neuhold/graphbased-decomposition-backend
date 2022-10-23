package ch.uzh.ifi.seal.monolith2microservices.controllers;

import ch.uzh.ifi.seal.monolith2microservices.conversion.GraphRepresentation;
import monolith2microservice.shared.dto.DecompositionDto;
import ch.uzh.ifi.seal.monolith2microservices.models.graph.Decomposition;
import ch.uzh.ifi.seal.monolith2microservices.persistence.DecompositionRepository;
import ch.uzh.ifi.seal.monolith2microservices.persistence.RepositoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class MicroservicesController {

    private Logger logger = LoggerFactory.getLogger(MicroservicesController.class);

    @Autowired
    DecompositionRepository decompositionRepository;

    @Autowired
    RepositoryRepository repositoryRepository;


    @CrossOrigin
    @RequestMapping(value="/microservices/{decompositionId}", method= RequestMethod.GET)
    public ResponseEntity<Set<GraphRepresentation>> getMicroservice(@PathVariable long decompositionId) throws Exception{
        Decomposition decomposition = decompositionRepository.findById(decompositionId);
        Set<GraphRepresentation> graph = decomposition.getServices().stream().map(GraphRepresentation::from).collect(Collectors.toSet());
        return new ResponseEntity<Set<GraphRepresentation>>(graph, HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/microservices", method = RequestMethod.GET)
    public ResponseEntity<List<DecompositionDto>> getMicroservices() throws Exception{
        List<Decomposition> decompositions = decompositionRepository.findAll();

        List<DecompositionDto> dtos = new ArrayList<>();

        for(Decomposition d: decompositions){
            DecompositionDto dto = new DecompositionDto();
            dto.setDecompositionId(d.getId());
            dto.setRepo(d.getRepository());
            dto.setParameters(d.getParameters());
            if(dto.getRepo() != null){
                dtos.add(dto);
            }
        }

        return new ResponseEntity<List<DecompositionDto>>(dtos, HttpStatus.OK);
    }


}
