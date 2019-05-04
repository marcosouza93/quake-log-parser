package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enumeration.PlayerRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.WORLD_PLAYER_CODE_MARKER;

@Slf4j
@Service
public class PlayerManager {

  @Autowired
  private DataSearchEngine dataSearchEngine;

  /**
   * Registers a new game player
   *
   * @param players
   *        The map that represents all players registered in the game
   * @param kills
   *        The map that represents the number of times each player killed an opponent
   * @param row
   *        The text base used to get player information
   */
  public void registerNew(Map<String, String> players, Map<String, Integer> kills, String row) {
    final String userCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_CODE.getLabel());
    final String userName = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_NAME.getLabel());

    players.put(userCode, userName);
    kills.put(players.get(userCode), Integer.valueOf(0));
  }

  /**
   * Collects points to the game players
   *
   * @param players
   *        The map that represents all players registered in the game
   * @param kills
   *        The map that represents the number of times each player killed an opponent
   * @param row
   *        The text used to get player information
   */
  public void collectScore(Map<String, String> players, Map<String, Integer> kills, String row) {
    final String killerCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLER_CODE.getLabel());
    final String killedPlayerCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel());

    // Checks if the world is the killer in this turn or if the killer and the dead player are the same person
    if (killerCode.equals(WORLD_PLAYER_CODE_MARKER.getLabel()) || killerCode.equals(killedPlayerCode)) {
      decreasePlayerScore(players, kills, killedPlayerCode);
    } else {
      increasePlayerScore(players, kills, killerCode);
      decreasePlayerScore(players, kills, killedPlayerCode);
    }
  }

  private void decreasePlayerScore(Map<String, String> players, Map<String, Integer> kills, String killedPlayerCode) {
    kills.put(players.get(killedPlayerCode), kills.get(players.get(killedPlayerCode)) - Integer.valueOf(1));
  }

  private void increasePlayerScore(Map<String, String> players, Map<String, Integer> kills, String killerCode) {
      kills.put(players.get(killerCode), kills.get(players.get(killerCode)) + Integer.valueOf(1));
  }

}
