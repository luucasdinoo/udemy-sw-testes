package br.com.dino.planetapi;

import br.com.dino.planetapi.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static br.com.dino.planetapi.common.PlanetsConstants.PLANET;
import static br.com.dino.planetapi.common.PlanetsConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
// Testes endtwoend
public class PlanetIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebTestClient webTestClient;

    // Utilizando WebTestClient
    @Test
    public void createPlanet_ReturnsCreated() {
        Planet sut = webTestClient.post().uri("/planets").bodyValue(PLANET)
                .exchange().expectStatus().isCreated().expectBody(Planet.class)
                .returnResult().getResponseBody();

        assertThat(sut.getId()).isNotNull();
    }

    // Utilizando TestRestTemplate
    @Test
    public void createPlanet_ReturnCreated(){
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanetsById_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetsByName_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(3);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByClimates_ReturnsAllPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsAllPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void removePlanetById_ReturnsNoContent(){
        // Exchange -> método genérico portanto o método utilizado no teste terá q ser informado nos parâmetros
        ResponseEntity<Void> sut = restTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
