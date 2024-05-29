package br.com.dino.planetapi.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static br.com.dino.planetapi.common.PlanetsConstants.PLANET;
import static br.com.dino.planetapi.common.PlanetsConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@SpringBootTest(classes = PlanetRepository.class)
// Testes de integração
@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){
        Planet planet = planetRepository.save(PLANET);
        Planet sut = testEntityManager.find(Planet.class, planet.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnThrwsException(){
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("","","");

        assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrwsException(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOpt = planetRepository.findById(planet.getId());
        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsPlanet(){
        Optional<Planet> planetOpt = planetRepository.findById(1L);
        assertThat(planetOpt).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOpt = planetRepository.findByName(planet.getName());
        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsPlanet(){
        String unexistingName = "unexisting";
        Optional<Planet> planetOpt = planetRepository.findByName(unexistingName);
        assertThat(planetOpt).isEmpty();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception{
        Example<Planet> queryWithOutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> responseWithOutFilters = planetRepository.findAll(queryWithOutFilters);
        List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithOutFilters).isNotEmpty();
        assertThat(responseWithOutFilters).hasSize(3);
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception{
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());
        List<Planet> response = planetRepository.findAll(query);
        assertThat(response).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDataBase(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        planetRepository.deleteById(planet.getId());
        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }

/*    @Test
    public void removePlanet_WithUnexistingId_ThrowsException(){
        assertThatThrownBy(() -> planetRepository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);
    }*/
}
