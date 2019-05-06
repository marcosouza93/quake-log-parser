package br.com.labs.quakelogparser.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.labs.quakelogparser.domain.Game;
import br.com.labs.quakelogparser.usecase.exception.UnreadableFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

public class GameReportGeneratorUnitTest {

    @InjectMocks
    private GameReportGenerator gameReportGenerator;

    @Mock
    private GameManager gameManager;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGenerateGameReportReadingTheLogFile() {
        // When is tried to generate a game report
        when(gameManager.getGames()).thenReturn(new ArrayList<>());
        List<Game> gameReport = gameReportGenerator.generate();

        // Then is processed the game and returned a game report
        verify(gameManager, VerificationModeFactory.atLeast(1)).process(any());
        Assert.assertNotNull(gameReport);
    }

    @Test(expected = UnreadableFileException.class)
    public void shouldNotReadTheLogFile() {
        // Given a runtime exception when it is called the game manager to process a game
        doThrow(new NoSuchElementException()).when(gameManager).process(any());

        try {
            // When is tried to generate a game report
            gameReportGenerator.generate();
        } catch (UnreadableFileException ex) {
            // Then is returned a known exception
            throw ex;
        }
    }
}
