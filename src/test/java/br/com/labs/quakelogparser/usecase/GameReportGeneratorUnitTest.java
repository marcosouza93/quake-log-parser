package br.com.labs.quakelogparser.usecase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

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
  public void generateGameReport() {
    // When is tried to generate a game report
    gameReportGenerator.generate();

    // Then is processed the game and returned a game report
    verify(gameManager, VerificationModeFactory.atLeast(1)).process(any(String.class));
  }

  @Test(expected = Exception.class)
  public void notGenerateGameReport() {
    // Given an exception when it is called the game manager to process a game
    doThrow(new Exception()).when(gameManager).process(any(String.class));

    try {
      // When is tried to generate a game report
      gameReportGenerator.generate();
    } catch (Exception ex) {
      // Then is returned an exception
      throw ex;
    }
  }
}
