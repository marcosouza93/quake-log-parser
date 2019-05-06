package br.com.labs.quakelogparser.http.json.mapper;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.domain.Player;
import br.com.labs.quakelogparser.http.json.GameDetailJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class GameDetailJsonResponseMapperUnitTest {

    @Test
    public void shouldMapperGameToGameDetailJsonResponse() {
        // Given a game list
        List<Game> games = populateGameList();

        // When is tried to mapping the games object to the games response object
        List<GameDetailJsonResponse> gamesDetail = GameDetailJsonResponseMapper
            .toGamesDetailJsonResponse(games);

        // Then is mapped the game response object
        Assert.assertNotNull(gamesDetail);
        Assert.assertEquals(gamesDetail.iterator().next().getKillsQuantity(), Integer.valueOf(300));
        Assert.assertTrue(gamesDetail.iterator().next().getKills().containsKey("Player name"));
        Assert.assertEquals(gamesDetail.iterator().next().getKills().get("Player name"),
            Integer.valueOf(100));
        Assert.assertEquals(gamesDetail.iterator().next().getPlayers().iterator().next(),
            "Player name");
        Assert.assertEquals(gamesDetail.size(), 3);
    }

    private List<Game> populateGameList() {
        List<Game> games = new ArrayList<>();
        games.add(createGame());
        games.add(createGame());
        games.add(createGame());

        return games;
    }

    private Game createGame() {
        Game game = new Game();

        game.setKillsQuantity(Integer.valueOf(300));
        game.setPlayers(createPlayers());

        return game;
    }

    private Map<String, Player> createPlayers() {
        Map<String, Player> players = new HashMap<>();

        players.put("1", createPlayer());
        players.put("2", createPlayer());
        players.put("3", createPlayer());

        return players;
    }

    private Player createPlayer() {
        Player player = new Player();

        player.setName("Player name");
        player.setKillsQuantity(Integer.valueOf(100));

        return player;
    }

}
