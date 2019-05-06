package br.com.labs.quakelogparser.usecase;

import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.INIT_GAME_MARKER;
import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.KILL_MARKER;
import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.NEW_PLAYER_MARKER;
import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.SHUT_DOWN_MARKER;
import static br.com.labs.quakelogparser.domain.enumeration.GameMarkers.WORLD_PLAYER_CODE_MARKER;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.domain.Player;
import br.com.labs.quakelogparser.domain.enumeration.PlayerRegex;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameManager {

    @Autowired
    private DataSearchEngine dataSearchEngine;

    private List<Game> games = new ArrayList<>();
    private Game game;
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
                registerNewPlayer(row);

            } else if (isAnInstructionToRegisterNewDeath(row)) {
                increaseGameKillsQuantity();
                collectScore(row);

            } else if (isAnInstructionToFinishTheGame(row)) {
                finishCurrentGame();
            }
        }
    }

    /**
     * Registers a new game player
     *
     * @param row
     *        The text base used to get player information
     */
    private void registerNewPlayer(String row) {
        final String userCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_CODE.getLabel());
        final String userName = dataSearchEngine.find(row, PlayerRegex.TO_GET_PLAYER_NAME.getLabel());

        Player player = new Player();
        player.setName(userName);

        // Checks if the player is already registered in the current game and if so, its score is recovered
        if (game.getPlayers().get(userCode) != null) {
            player.setKillsQuantity(game.getPlayers().get(userCode).getKillsQuantity());
        }
        game.getPlayers().put(userCode, player);
    }

    /**
     * Collects points for the game players
     *
     * @param row
     *        The text base used to get player information
     */
    private void collectScore(String row) {
        final String killerCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLER_CODE.getLabel());
        final String killedPlayerCode = dataSearchEngine.find(row, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel());

        // Checks if the world is the killer in this turn or if the killer and the killed player are the same person
        if (killerCode.equals(WORLD_PLAYER_CODE_MARKER.getLabel()) || killerCode.equals(killedPlayerCode)) {
            registerNegativePointForTheKilledPlayer(killedPlayerCode);
        } else {
            registerPositivePointForTheKiller(killerCode);
        }
    }

    /**
     * Registers a negative point for the killed player in this turn
     *
     * @param killedPlayerCode
     *        The killed player in this turn
     */
    private void registerNegativePointForTheKilledPlayer(String killedPlayerCode) {
        Player player = game.getPlayers().get(killedPlayerCode);
        player.setKillsQuantity(player.getKillsQuantity() - Integer.valueOf(1));

        game.getPlayers().put(killedPlayerCode, player);
    }

    /**
     * Registers a positive point for the killer in this turn
     *
     * @param killerCode
     *        The killer in this turn
     */
    private void registerPositivePointForTheKiller(String killerCode) {
        Player player = game.getPlayers().get(killerCode);
        player.setKillsQuantity(player.getKillsQuantity() + Integer.valueOf(1));

        game.getPlayers().put(killerCode, player);
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

    private void initNewGame() {
        game = new Game();
        setGameStarted(true);
    }

    private void increaseGameKillsQuantity() {
        game.setKillsQuantity(game.getKillsQuantity() + Integer.valueOf(1));
    }

    private void finishCurrentGame() {
        games.add(game);
        setGameStarted(false);
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getGame() {
        return game;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
