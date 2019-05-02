package br.com.labs.quakelogparser.http.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonSerialize
public class GameDetailJsonResponse {

  @JsonProperty("total_kills")
  private Integer killsQuantity;

  private List<String> players;

  private Map<String, Integer> kills;
}
