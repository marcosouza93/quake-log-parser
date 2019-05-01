package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class GameReportGenerator {

  @Autowired private GameManager game;

  private static final String FILE_NAME = "/data/games.log";

  /**
   * Reads all lines from a file as a {@code Stream}. Bytes from the file are decoded into
   * characters.
   *
   * @return The games report as {@link List}
   * @throws Exception
   */
  public List<Game> generate() {
    try (Stream<String> stream = getLogFileAsStream()) {
      stream.forEach(game::process);

    } catch (Exception e) {
      log.error("An error occurred processing the log file", e);
      throw e;
    }

    return game.getGames();
  }

  private Stream<String> getLogFileAsStream() {
    InputStream in = getClass().getResourceAsStream(FILE_NAME);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    return reader.lines();
  }
}
