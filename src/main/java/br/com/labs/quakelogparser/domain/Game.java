package br.com.labs.quakelogparser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

  private Integer killsQuantity = 0;
  private List<String> players;
  private Map<String, Integer> kills;
}
