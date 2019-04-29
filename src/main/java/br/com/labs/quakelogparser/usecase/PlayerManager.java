package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static br.com.labs.quakelogparser.domain.enums.GamesMarker.WORLD_PLAYER_MARKER;
import static br.com.labs.quakelogparser.domain.enums.PlayerRegex.*;

@Slf4j
@Service
public class PlayerManager {

  @Autowired private DataSearchEngine dataSearchEngine;

  public void registerNew(Map<String, String> players, String row) {
    final String userCode = dataSearchEngine.find(row, USER_CODE.label());
    final String userName = dataSearchEngine.find(row, USER_NAME.label());

    players.put(userCode, userName);
  }

  public void collectScore(Game game, String row) {
    final String killer = dataSearchEngine.find(row, KILLER.label());
    final String killed = dataSearchEngine.find(row, KILLED.label());

    if (row.contains(WORLD_PLAYER_MARKER.label())) {
      decreasePlayerScore(game, killed);

    } else {
      increasePlayerScore(game, killer, killed);
    }
  }

  private void decreasePlayerScore(Game game, String killed) {
    if (game.getKills().containsKey(killed)) {
      game.getKills().put(killed, game.getKills().get(killed) - 1);
    } else {
      game.getKills().put(killed, -1);
    }
  }

  private void increasePlayerScore(Game game, String killer, String killed) {
    if (!killer.equals(killed)) {
      if (game.getKills().containsKey(killer)) {
        game.getKills().put(killer, game.getKills().get(killer) + 1);
      } else {
        game.getKills().put(killer, 1);
      }
    }
  }
}
