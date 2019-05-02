package br.com.labs.quakelogparser.usecase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class GameManagerUnitTest {

  @InjectMocks
  private GameManager gameManager;

  @Mock
  private PlayerManager playerManager;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void initNewGame() {
    // Given a text with an init game marker
    String text =
        " 20:37 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate\\10000\\sv_minRate\\0\\sv_hostname\\Code Miner Server\\g_gametype\\0\\sv_privateClients\\2\\sv_maxclients\\16\\sv_allowDownload\\0\\bot_minplayers\\0\\dmflags\\0\\fraglimit\\20\\timelimit\\15\\g_maxGameClients\\0\\capturelimit\\8\\version\\ioq3 1.36 linux-x86_64 Apr 12 2009\\protocol\\68\\mapname\\q3dm17\\gamename\\baseq3\\g_needpass\\0\n";

    // When is processed the game manager
    gameManager.process(text);

    // Then is started a new game
    Assert.assertTrue(gameManager.isGameStarted());
    Assert.assertNotNull(gameManager.getGame());
  }

  @Test
  public void registerNewPlayer() {
    // Given a text with a new player marker
    String text =
        " 21:51 ClientUserinfoChanged: 3 n\\Dono da Bola\\t\\0\\model\\sarge/krusade\\hmodel\\sarge/krusade\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
    // And It was already started a new game
    initNewGame();

    // When is processed the game manager
    gameManager.process(text);

    // Then is registered a new player to the game
    verify(playerManager, VerificationModeFactory.times(1)).registerNew(any(), any(String.class));
  }

  @Test
  public void collectScoreToPlayer() {
    // Given a text with a kill marker
    String text = "  2:11 Kill: 2 4 6: Dono da Bola killed Zeh by MOD_ROCKET\n";
    // And It was already started a new game
    initNewGame();

    // When is processed the game manager
    gameManager.process(text);

    // Then is collected score to the player and increased the game score
    verify(playerManager, VerificationModeFactory.times(1)).collectScore(any(), any(String.class));
    Assert.assertEquals(gameManager.getGame().getKillsQuantity(), Integer.valueOf(1));
  }

  @Test
  public void finishCurrentGame() {
    // Given a text with a shutdown marker
    String text = " 26  0:00 ------------------------------------------------------------\n";
    // And It was already started a new game
    initNewGame();

    // When is processed the game manager
    gameManager.process(text);

    // Then is finished the game
    Assert.assertFalse(gameManager.isGameStarted());
  }
}
