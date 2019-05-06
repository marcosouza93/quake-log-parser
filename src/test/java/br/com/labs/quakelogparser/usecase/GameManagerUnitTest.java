package br.com.labs.quakelogparser.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.labs.quakelogparser.domain.enumeration.PlayerRegex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

public class GameManagerUnitTest {

    @InjectMocks
    private GameManager gameManager;

    @Mock
    private DataSearchEngine dataSearchEngine;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldStartNewGame() {
        // Given a text with an init game marker
        String text =
            " 20:37 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate\\"
                + "10000\\sv_minRate\\0\\sv_hostname\\Code Miner Server\\g_gametype\\0\\sv_pri"
                + "vateClients\\2\\sv_maxclients\\16\\sv_allowDownload\\0\\bot_minplayers\\0\\"
                + "dmflags\\0\\fraglimit\\20\\timelimit\\15\\g_maxGameClients\\0\\capturelimit"
                + "\\8\\version\\ioq3 1.36 linux-x86_64 Apr 12 2009\\protocol\\68\\mapname\\q3"
                + "dm17\\gamename\\baseq3\\g_needpass\\0\n";

        // When is processed the game manager
        gameManager.process(text);

        // Then is started a new game
        Assert.assertTrue(gameManager.isGameStarted());
        Assert.assertNotNull(gameManager.getGame());
    }

    @Test
    public void shouldRegisterNewPlayer() {
        // Given a text with a new player marker
        String text =
            " 21:51 ClientUserinfoChanged: 3 n\\Dono da Bola\\t\\0\\model\\sarge/krusade\\hmod"
                + "el\\sarge/krusade\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\95\\w\\0\\l\\"
                + "0\\tt\\0\\tl\\0\n";
        // And It was already started a new game
        shouldStartNewGame();

        // When is searched a player code and a player name in the text
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_CODE.getLabel()))
            .thenReturn("3");
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_NAME.getLabel()))
            .thenReturn("Dono da Bola");
        // And is tried to register a new player in the game
        gameManager.process(text);

        // Then is registered a new player in the game
        verify(dataSearchEngine,
            VerificationModeFactory.times(2)).find(any(), any());
        Assert.assertNotNull(gameManager.getGame().getPlayers());
        Assert.assertTrue(gameManager.getGame().getPlayers().containsKey("3"));
        Assert.assertEquals(gameManager.getGame().getPlayers().get("3").getName(),
            "Dono da Bola");
    }

    @Test
    public void shouldChangeTheNameOfAPlayerAlreadyRegistered() {
        // Given a text with a new player marker
        String text =
            " 21:51 ClientUserinfoChanged: 3 n\\Dono da Bola mais antiga do mundo\\t\\0\\model"
                + "\\sarge/krusade\\hmodel\\sarge/krusade\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2"
                + "\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
        // And It was already started a new game and It was already registered a player in the game
        shouldStartNewGame();
        shouldRegisterNewPlayer();

        // When is searched a player code and a player name in the text
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_CODE.getLabel()))
            .thenReturn("3");
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_NAME.getLabel()))
            .thenReturn("Dono da Bola mais antiga do mundo");
        // And is tried to change the name of the player already registered
        gameManager.process(text);

        // Then is changed the name of the player already registered
        verify(dataSearchEngine,
            VerificationModeFactory.times(4)).find(any(), any());
        Assert.assertNotNull(gameManager.getGame().getPlayers());
        Assert.assertTrue(gameManager.getGame().getPlayers().containsKey("3"));
        Assert.assertEquals(gameManager.getGame().getPlayers().get("3").getName(),
            "Dono da Bola mais antiga do mundo");
    }

    @Test
    public void shouldRegisterPositivePointForTheKiller() {
        // Given a text with a kill marker
        String text = "  2:11 Kill: 3 4 6: Dono da Bola killed Zeh by MOD_ROCKET\n";
        // And It was already started a new game and It was already registered a player
        shouldStartNewGame();
        shouldRegisterNewPlayer();

        // When is searched a killer code and a killed player code in the text
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel()))
            .thenReturn("3");
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
            .thenReturn("4");
        // And is tried to register a positive point for the killer
        gameManager.process(text);

        // Then is registered a positive point for the killer
        verify(dataSearchEngine,
            VerificationModeFactory.times(4)).find(any(), any());
        Assert.assertEquals(gameManager.getGame().getKillsQuantity(), Integer.valueOf(1));
        Assert.assertEquals(gameManager.getGame().getPlayers().get("3").getKillsQuantity(),
            Integer.valueOf(1));
    }

    @Test
    public void shouldRegisterNegativePointForTheKilledPlayerWhenTheKillerIsTheSamePlayer() {
        // Given a text with a kill marker
        String text = "  2:11 Kill: 3 3 6: Dono da Bola killed Dono da Bola by MOD_ROCKET\n";
        // And It was already started a new game and It was already registered a player
        shouldStartNewGame();
        shouldRegisterNewPlayer();

        // When is searched a killer code and a killed player code in the text
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel()))
            .thenReturn("3");
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
            .thenReturn("3");
        // And is tried to register a negative point for the killed player because the killer
        // is the same person
        gameManager.process(text);

        // Then is registered a negative point for the killed player
        verify(dataSearchEngine,
            VerificationModeFactory.times(4)).find(any(), any());
        Assert.assertEquals(gameManager.getGame().getKillsQuantity(), Integer.valueOf(1));
        Assert.assertEquals(gameManager.getGame().getPlayers().get("3").getKillsQuantity(),
            Integer.valueOf(-1));
    }

    @Test
    public void shouldRegisterNegativePointForTheKilledPlayerWhenTheKillerIsTheWorld() {
        // Given a text with a kill marker
        String text = "  2:11 Kill: 1022 3 6: <world> killed Dono da Bola by MOD_ROCKET\n";
        // And It was already started a new game and It was already registered a player
        shouldStartNewGame();
        shouldRegisterNewPlayer();

        // When is searched a killer code and a killed player code in the text
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel()))
            .thenReturn("1022");
        when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
            .thenReturn("3");
        // And is tried to register a negative point for the killed player because the killer
        // is the world
        gameManager.process(text);

        // Then is registered a negative point for the killed player
        verify(dataSearchEngine,
            VerificationModeFactory.times(4)).find(any(), any());
        Assert.assertEquals(gameManager.getGame().getKillsQuantity(), Integer.valueOf(1));
        Assert.assertEquals(gameManager.getGame().getPlayers().get("3").getKillsQuantity(),
            Integer.valueOf(-1));
    }

    @Test
    public void shouldFinishCurrentGame() {
        // Given a text with a shutdown marker
        String text = " 26  0:00 ------------------------------------------------------------\n";
        // And It was already started a new game
        shouldStartNewGame();

        // When is processed the game manager
        gameManager.process(text);

        // Then is finished the game
        Assert.assertFalse(gameManager.isGameStarted());
        Assert.assertNotNull(gameManager.getGame());
        Assert.assertNotNull(gameManager.getGames());
    }
}
