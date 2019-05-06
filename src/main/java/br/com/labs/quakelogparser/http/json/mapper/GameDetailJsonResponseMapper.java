package br.com.labs.quakelogparser.http.json.mapper;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.http.json.GameDetailJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GameDetailJsonResponseMapper {

    public static List<GameDetailJsonResponse> toGamesDetailJsonResponse(List<Game> games) {
        List<GameDetailJsonResponse> gamesResponse = new ArrayList<>();

        games.forEach(
            game -> {
                GameDetailJsonResponse gameResponse = new GameDetailJsonResponse();
                List<String> playersResponse = new ArrayList<>();
                Map<String, Integer> killsResponse = new HashMap<>();

                game.getPlayers()
                    .forEach(
                        (playerCode, player) -> {
                            playersResponse.add(player.getName());
                            killsResponse.put(player.getName(), player.getKillsQuantity());
                        });

                gameResponse.setPlayers(playersResponse);
                gameResponse.setKills(killsResponse);
                gameResponse.setKillsQuantity(game.getKillsQuantity());
                gamesResponse.add(gameResponse);
            });

        return gamesResponse;
    }
}
