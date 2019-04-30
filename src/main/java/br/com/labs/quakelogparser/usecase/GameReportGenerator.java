package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class GameReportGenerator {

  @Autowired private GameManager game;

  @Value("classpath:data/games.log")
  Resource resourceFile;

  /**
   * Reads all lines from a file as a {@code Stream}. Bytes from the file are decoded into
   * characters.
   *
   * @return The games report as {@link List}
   * @throws Exception
   */
  public List<Game> generate() throws Exception {
    try (Stream<String> stream = Files.lines(Paths.get(resourceFile.getURI()))) {
      stream.forEach(game::process);

    } catch (Exception e) {
      log.error("An error occurred processing the log file", e);
      throw e;
    }

    return game.getGames();
  }
}
