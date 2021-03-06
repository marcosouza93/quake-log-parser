package br.com.labs.quakelogparser.domain;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {

    private Integer killsQuantity = 0;
    private Map<String, Player> players = new HashMap<>();

}
