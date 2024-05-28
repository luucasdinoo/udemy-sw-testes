package br.com.dino.planetapi.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
// Imports estáticos
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.dino.planetapi.common.PlanetsConstants.PLANET;
import static br.com.dino.planetapi.common.PlanetsConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// @SpringBootTest(classes = PlanetService.class)
// Testes unitários
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    // @Autowired
    @InjectMocks
    private PlanetService planetService;

    // @MockBean
    @Mock
    private PlanetRepository planetRepository;

    @Test
    // operação_estado_retorno
    public void createPlanet_WithValidData_ReturnsPlanet(){
        when(planetRepository.save(PLANET)).thenReturn(PLANET);
        Planet sut = planetService.create(PLANET); // sut -> system under test / sistema sob teste
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WitchInvalidData_ThrowsException(){
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanetById_ByExistingId_ReturnsPlanet(){
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = planetService.getById(1L);
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetById_ByUnexistingId_ReturnsPlanet(){
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Planet> sut = planetService.getById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanetByName_ByEexistingName_ReturnsPlanet(){
        when(planetRepository.findByName("name")).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = planetService.getByName("name");
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetByName_ByUnexistingName_ReturnsEmpty(){
        final String name = "Unexisting name";
        when(planetRepository.findByName(name)).thenReturn(Optional.empty());
        Optional<Planet> sut = planetService.getByName(name);
        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_RetornAllPlanets(){
        List<Planet> planets = new ArrayList<>(){
            {
                add(PLANET);
            }
        };
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));
        when(planetRepository.findAll(query)).thenReturn(planets);
        List<Planet> sut = planetService.getAll(PLANET.getTerrain(), PLANET.getClimate());
        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_RetornNoPlanets(){
        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());
        List<Planet> sut = planetService.getAll(PLANET.getTerrain(), PLANET.getClimate());
        assertThat(sut).isEmpty();
    }

}
