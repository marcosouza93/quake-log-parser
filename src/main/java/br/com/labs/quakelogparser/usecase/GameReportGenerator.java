package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.usecase.exception.UnreadableFileException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameReportGenerator {

    @Autowired
    private GameManager game;

    private static final String FILE_NAME = "/data/games.log";

    /**
     * Reads all lines from a file as a {@code Stream}. Bytes from the file are decoded into
     * characters.
     *
     * @return The games report as {@link List}
     */
    public List<Game> generate() {
        try (Stream<String> stream = getLogFileAsStream()) {
            stream.forEach(game::process);

        } catch (UncheckedIOException | NoSuchElementException ex) {
            log.error("An error occurred while processing the log file", ex);
            throw new UnreadableFileException(ex);
        }

        return game.getGames();
    }

    private Stream<String> getLogFileAsStream() {
        InputStream in = getClass().getResourceAsStream(FILE_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        return reader.lines();
    }
}
