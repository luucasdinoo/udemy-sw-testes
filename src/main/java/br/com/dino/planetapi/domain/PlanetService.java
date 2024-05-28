package br.com.dino.planetapi.domain;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository){
        this.planetRepository = planetRepository;
    }

    public Planet create (Planet planet){
        return planetRepository.save(planet);
    }

    public Optional<Planet> getById(Long id){
        return planetRepository.findById(id);
    }

    public Optional<Planet> getByName(String name){
        return planetRepository.findByName(name);
    }

    public List<Planet> getAll(String terrain, String climate){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate, terrain));
        return planetRepository.findAll(query);
    }
}
