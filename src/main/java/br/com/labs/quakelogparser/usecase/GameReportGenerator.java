package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
public class GameReportGenerator {

  private static final String KILL_MARKER = "Kill";
  private static final String INIT_GAME_MARKER = "InitGame";
  private static final String SHUT_DOWN_MARKER =
      "------------------------------------------------------------";
  private static final String NEW_PLAYER_MARKER = "ClientUserinfoChanged";
  private static final String WORLD_PLAYER_MARKER = "<world>";

  private final String regexToGetUserCode = "ClientUserinfoChanged\\:\\s(\\d)";
  private final String regexToGetUserName = "\\sn\\\\([\\w\\W]+)\\\\t\\\\";
  private final String regexToGetKiller = "\\d:\\s([\\w\\W]+)\\skilled";
  private final String regexToGetDeadPlayer = "killed\\s([\\w\\W]+)\\sby";

  private List<Game> games = new ArrayList<>();
  private Game game;
  private Map<String, String> players;
  private Map<String, Integer> kills;
  private boolean isGameStarted = false;

  @Value("classpath:data/games.log")
  Resource resourceFile;

  public List<Game> generate() throws Exception {
    try (Stream<String> stream = Files.lines(Paths.get(resourceFile.getURI()))) {
      stream.forEach(
          row -> {
            if (row.contains(INIT_GAME_MARKER)) {
              game = new Game();
              players = new HashMap<>();
              kills = new HashMap<>();
              isGameStarted = true;
            } else {
              if (row.contains(NEW_PLAYER_MARKER)) {
                final Pattern patternToGetUserCode = Pattern.compile(regexToGetUserCode);
                final Matcher userCodeMatcher = patternToGetUserCode.matcher(row);
                userCodeMatcher.find();

                final Pattern patternToGetUserName = Pattern.compile(regexToGetUserName);
                final Matcher userNameMatcher = patternToGetUserName.matcher(row);
                userNameMatcher.find();

                players.put(userCodeMatcher.group(1), userNameMatcher.group(1));

              } else if (row.contains(KILL_MARKER)) {
                final Pattern patternToGetKiller = Pattern.compile(regexToGetKiller);
                final Matcher killerMatcher = patternToGetKiller.matcher(row);
                killerMatcher.find();

                game.setKillsQuantity(game.getKillsQuantity() + 1);

                final Pattern patternToGetDeadPlayer = Pattern.compile(regexToGetDeadPlayer);
                final Matcher deadPlayerMatcher = patternToGetDeadPlayer.matcher(row);
                deadPlayerMatcher.find();

                if (row.contains(WORLD_PLAYER_MARKER)) {
                  if (kills.containsKey(deadPlayerMatcher.group(1))) {
                    kills.put(
                        deadPlayerMatcher.group(1), kills.get(deadPlayerMatcher.group(1)) - 1);
                  } else {
                    kills.put(deadPlayerMatcher.group(1), -1);
                  }

                } else {
                  if (!killerMatcher.group(1).equals(deadPlayerMatcher.group(1))) {
                    if (kills.containsKey(killerMatcher.group(1))) {
                      kills.put(killerMatcher.group(1), kills.get(killerMatcher.group(1)) + 1);
                    } else {
                      kills.put(killerMatcher.group(1), 1);
                    }
                  }
                }

              } else if (isGameStarted && row.contains(SHUT_DOWN_MARKER)) {
                game.setPlayers(new ArrayList<>(players.values()));
                game.setKills(kills);
                games.add(game);
                isGameStarted = false;
              }
            }
          });

    } catch (Exception e) {
      log.error("An error occurred processing the log file", e);
      throw e;
    }

    return games;
  }
}
