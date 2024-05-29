package br.com.dino.planetapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name = "planets")
@Getter @Setter @NoArgsConstructor
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;
    @NotEmpty
    @Column(nullable = false)
    private String climate;
    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    public Planet(String name, String climate, String terrain){
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String climate, String terrain){
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(Long id, String name, String climate, String terrain){
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}
