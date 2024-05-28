package br.com.dino.planetapi.web;

import br.com.dino.planetapi.domain.Planet;
import br.com.dino.planetapi.domain.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private  PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create (@RequestBody Planet planet){
        Planet planetCreated = planetService.create(planet);
        return  ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable Long id){
        return  planetService.getById(id).map(planet -> ResponseEntity.ok(planet)) // .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public  ResponseEntity<Planet> getByName(@PathVariable String name){
        return planetService.getByName(name).map(planet -> ResponseEntity.ok(planet))
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getAll(@RequestParam (required = false) String terrain,
                                               @RequestParam (required = false) String climate){
        List<Planet> list = planetService.getAll(terrain, climate);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        planetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
