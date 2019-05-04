package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.*;

@Slf4j
@Service
public class GameManager {

  @Autowired
  PlayerManager player;

  private List<Game> games = new ArrayList<>();
  private Game game;
  private Map<String, String> players;
  private boolean gameStarted = false;

  /**
   * Processes the game using key markers to identify all valid scenarios in the game
   *
   * @param row
   *        Each row from the file that represents game instructions
   */
  public void process(String row) {
    if (isAnInstructionToStartNewGame(row)) {
      initNewGame();
    } else {
      if (isAnInstructionToRegisterNewPlayer(row)) {
        player.registerNew(players, game.getKills(), row);

      } else if (isAnInstructionToRegisterNewDeath(row)) {
        increaseGameKillQuantity();
        player.collectScore(players, game.getKills(), row);

      } else if (isAnInstructionToFinishTheGame(row)) {
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

  private boolean isAnInstructionToStartNewGame(String text) {
    return text.contains(INIT_GAME_MARKER.getLabel());
  }

  private boolean isAnInstructionToRegisterNewPlayer(String text) {
    return text.contains(NEW_PLAYER_MARKER.getLabel());
  }

  private boolean isAnInstructionToRegisterNewDeath(String text) {
    return text.contains(KILL_MARKER.getLabel());
  }

  private boolean isAnInstructionToFinishTheGame(String text) {
    return isGameStarted() && text.contains(SHUT_DOWN_MARKER.getLabel());
  }

  private void increaseGameKillQuantity() {
    game.setKillsQuantity(game.getKillsQuantity() + Integer.valueOf(1));
  }

  public List<Game> getGames() {
    return games;
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public Game getGame() {
    return game;
  }
}
