package br.com.labs.quakelogparser.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Game {

  private Integer killsQuantity = 0;
  private List<String> players;
  private Map<String, Integer> kills;

  public Game(final Map<String, Integer> kills) {
    this.kills = kills;
  }
}
