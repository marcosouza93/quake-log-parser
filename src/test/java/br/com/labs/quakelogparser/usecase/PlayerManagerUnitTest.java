package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enums.PlayerRegex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class PlayerManagerUnitTest {

  @InjectMocks
  private PlayerManager playerManager;

  @Mock
  private DataSearchEngine dataSearchEngine;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void registerNewPlayer() {
    // Given a empty map to register players and a text with game events
    Map<String, String> players = new HashMap<>();
    String text =
        " 20:38 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n";

    // When is searched a player code and a player name
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_CODE.getLabel())).thenReturn("2");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_NAME.getLabel()))
        .thenReturn("Isgalamido");
    // And is tried to register a new player to the game in the text
    playerManager.registerNew(players, text);

    // Then is registered a new player to the game
    Assert.assertNotNull(players);
    Assert.assertEquals(players.get("2"), "Isgalamido");
  }

  @Test
  public void collectPositiveScoreWhenTheKillerIsNew() {
    // Given a empty map to register deaths and a text with game events
    Map<String, Integer> kills = new HashMap<>();
    String text = " 1:08 Kill: 3 2 6: Isgalamido killed Mocinha by MOD_ROCKET\n";

    // When is searched a killer name and a killed player name in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER.getLabel()))
        .thenReturn("Isgalamido");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER.getLabel()))
        .thenReturn("Mocinha");
    // And is tried to collect positive score to the new killer
    playerManager.collectScore(kills, text);

    // Then is registered the value 1 to the new killer
    Assert.assertNotNull(kills);
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(1));
  }

  @Test
  public void increaseTheScoreOfTheKillerWhenHeIsAlreadyRegistered() {
    // Given a map with deaths register and a text with game events
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Isgalamido", Integer.valueOf(12));
    String text = " 1:08 Kill: 3 2 6: Isgalamido killed Mocinha by MOD_ROCKET\n";

    // When is searched a killer name and a killed player name in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER.getLabel()))
        .thenReturn("Isgalamido");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER.getLabel()))
        .thenReturn("Mocinha");
    // And is tried to collect positive score to the killer
    playerManager.collectScore(kills, text);

    // Then is increased the score of the killer
    Assert.assertNotNull(kills);
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(13));
  }

  @Test
  public void collectNegativeScoreWhenTheKilledPlayerIsNewAndHeWasKilledByTheWorld() {
    // Given a empty map to register deaths and a text with game events
    Map<String, Integer> kills = new HashMap<>();
    String text = " 21:42 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT\n";

    // When is searched a killer name and a killed player name in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER.getLabel())).thenReturn("<world>");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER.getLabel()))
        .thenReturn("Isgalamido");
    // And is tried to collect negative score to the new killed player because the killer is the
    // world
    playerManager.collectScore(kills, text);

    // Then is registered the value -1 to the new killed player
    Assert.assertNotNull(kills);
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(-1));
  }

  @Test
  public void decreaseTheScoreOfTheKilledPlayerWhenHeIsAlreadyRegisteredAndHeWasKilledByTheWorld() {
    // Given a map with deaths register and a text with game events
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Isgalamido", Integer.valueOf(12));
    String text = " 21:42 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT\n";

    // When is searched a killer name and a killed player name in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER.getLabel())).thenReturn("<world>");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER.getLabel()))
        .thenReturn("Isgalamido");
    // And is tried to collect negative score to the killed player because the killer is the world
    playerManager.collectScore(kills, text);

    // Then is decreased the score of the killed player
    Assert.assertNotNull(kills);
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(11));
  }
}
