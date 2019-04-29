package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.labs.quakelogparser.domain.enums.GamesMarker.*;

@Slf4j
@Service
@Getter
public class GameManager {

  private List<Game> games = new ArrayList<>();
  private Game game;
  private Map<String, String> players;
  private boolean gameStarted = false;

  @Autowired PlayerManager player;

  public void process(String row) {
    if (row.contains(INIT_GAME_MARKER.label())) {
      initNewGame();
    } else {
      if (row.contains(NEW_PLAYER_MARKER.label())) {
        player.registerNew(players, row);

      } else if (row.contains(KILL_MARKER.label())) {
        increaseGameKillQuantity();
        player.collectScore(game, row);

      } else if (isGameStarted() && row.contains(SHUT_DOWN_MARKER.label())) {
        finishCurrentGame();
      }
    }
  }

  private void initNewGame() {
    game = new Game(new HashMap<>());
    players = new HashMap<>();
    gameStarted = true;
  }

  private void finishCurrentGame() {
    game.setPlayers(new ArrayList<>(players.values()));
    games.add(game);
    gameStarted = false;
  }

  private void increaseGameKillQuantity() {
    game.setKillsQuantity(game.getKillsQuantity() + 1);
  }
}
