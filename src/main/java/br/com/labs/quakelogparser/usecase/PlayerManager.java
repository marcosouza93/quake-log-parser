package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enums.PlayerRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static br.com.labs.quakelogparser.domain.enums.GameMarkers.WORLD_PLAYER_MARKER;

@Slf4j
@Service
public class PlayerManager {

  @Autowired
  private DataSearchEngine dataSearchEngine;

  /**
   * Registers a new game player
   *
   * @param players
   *        The game users
   * @param row
   *        The text base used to get player information
   */
  public void registerNew(Map<String, String> players, String row) {
    final String userCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_CODE.getLabel());
    final String userName = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_NAME.getLabel());

    players.put(userCode, userName);
  }

  /**
   * Collects points to the game players
   *
   * @param kills
   *        The map that represents the number of times each player killed an
   *        opponent
   * @param row
   *        The text used to get player information
   */
  public void collectScore(Map<String, Integer> kills, String row) {
    final String killer = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLER.getLabel());
    final String killed = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLED_PLAYER.getLabel());

    // Checks if the world killed the current player
    if (row.contains(WORLD_PLAYER_MARKER.getLabel())) {
      decreasePlayerScore(kills, killed);
    } else {
      increasePlayerScore(kills, killer, killed);
    }
  }

  /**
   * Registers negative point to the dead player score
   * @param kills
   *        The map that represents the number of times each player killed an
   *        opponent
   * @param killed
   *        Killed player in this turn
   */
  private void decreasePlayerScore(Map<String, Integer> kills, String killed) {
    // Checks if the killed player is already registered on the death dictionary
    if (kills.containsKey(killed)) {
      kills.put(killed, kills.get(killed) - Integer.valueOf(1));
    } else {
      kills.put(killed, Integer.valueOf(-1));
    }
  }

  /**
   * Registers positive point to the killer score
   * @param kills
   *        The map that represents the number of times each player killed an
   *        opponent
   * @param killer
   *        Killer in this turn
   * @param killed
   *        Killed player in this turn
   */
  private void increasePlayerScore(Map<String, Integer> kills, String killer, String killed) {
    // Checks if the killer and the dead player are not the same person
    if (!killer.equals(killed)) {
      // Checks if the killer is already registered on the death dictionary
      if (kills.containsKey(killer)) {
        kills.put(killer, kills.get(killer) + Integer.valueOf(1));
      } else {
        kills.put(killer, Integer.valueOf(1));
      }
    }
  }

}
