package br.com.dino.planetapi.common;

import br.com.dino.planetapi.domain.Planet;

public class PlanetsConstants {

    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
}
