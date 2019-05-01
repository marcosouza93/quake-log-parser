package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enums.PlayerRegex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static br.com.labs.quakelogparser.domain.enums.GamesMarker.WORLD_PLAYER_MARKER;

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
  public void registerNewOne(Map<String, String> players, String row) {
    final String userCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_CODE.label());
    final String userName = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_NAME.label());

    players.put(userCode, userName);
  }

  /**
   * Collects points to the game players
   *
   * @param kills
   *        The death dictionary that represents the number of times each player killed an
   *        opponent
   * @param row
   *        The text base used to get player information
   */
  public void collectScore(Map<String, Integer> kills, String row) {
    final String killer = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLER.label());
    final String killed = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLED_PLAYER.label());

    // Checks if the world killed the current player
    if (row.contains(WORLD_PLAYER_MARKER.label())) {
      decreasePlayerScore(kills, killed);
    } else {
      increasePlayerScore(kills, killer, killed);
    }
  }

  /**
   * Registers negative point to the dead player score
   * @param kills
   *        The death dictionary that represents the number of times each player killed an
   *        opponent
   * @param killed
   *        Killer player in this turn
   */
  private void decreasePlayerScore(Map<String, Integer> kills, String killed) {
    // Checks if the killed player is already registered on the death dictionary
    if (kills.containsKey(killed)) {
      kills.put(killed, kills.get(killed) - 1);
    } else {
      kills.put(killed, -1);
    }
  }

  /**
   * Registers positive point to the killer score
   * @param kills
   *        The death dictionary that represents the number of times each player killed an
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
        kills.put(killer, kills.get(killer) + 1);
      } else {
        kills.put(killer, 1);
      }
    }
  }

}
