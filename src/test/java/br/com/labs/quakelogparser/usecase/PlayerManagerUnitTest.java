package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enumeration.PlayerRegex;
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
    // Given an empty map to register players
    Map<String, String> players = new HashMap<>();
    // And an empty map to register deaths
    Map<String, Integer> kills = new HashMap<>();
    // And a text with game events
    String text =
        " 20:38 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\uriel/zael\\hmodel\\uriel/zael\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n";

    // When is searched a player code and a player name
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_CODE.getLabel())).thenReturn("2");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_PLAYER_NAME.getLabel()))
        .thenReturn("Isgalamido");
    // And is tried to register a new player in the game
    playerManager.registerNew(players, kills, text);

    // Then is registered a new player in the game and initialized the kills map to the new player
    Assert.assertNotNull(players);
    Assert.assertNotNull(kills);
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(0));
    Assert.assertEquals(players.get("2"), "Isgalamido");
  }

  @Test
  public void increaseTheKillerScore() {
    // Given a map with player register
    Map<String, String> players = new HashMap<>();
    players.put("2", "Mocinha");
    players.put("3", "Isgalamido");
    // And a map with death register
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Mocinha", Integer.valueOf(9));
    kills.put("Isgalamido", Integer.valueOf(12));
    // And a text with game events
    String text = " 1:08 Kill: 3 2 6: Isgalamido killed Mocinha by MOD_ROCKET\n";

    // When is searched a killer code and a killed player code in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel())).thenReturn("3");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
        .thenReturn("2");
    // And is tried to collect positive score to the killer
    playerManager.collectScore(players, kills, text);

    // Then is increased the killer score
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(13));
  }

  @Test
  public void decreaseTheKilledPlayerScore() {
    // Given a map with player register
    Map<String, String> players = new HashMap<>();
    players.put("2", "Mocinha");
    players.put("3", "Isgalamido");
    // And a map with death register
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Mocinha", Integer.valueOf(12));
    kills.put("Isgalamido", Integer.valueOf(10));
    // And a text with game events
    String text = " 1:08 Kill: 3 2 6: Isgalamido killed Mocinha by MOD_ROCKET\n";

    // When is searched a killer code and a killed player code in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel())).thenReturn("3");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
            .thenReturn("2");
    // And is tried to collect negative score to the killed player
    playerManager.collectScore(players, kills, text);

    // Then is decreased the killed player score
    Assert.assertEquals(kills.get("Mocinha"), Integer.valueOf(11));
  }

  @Test
  public void decreaseTheKilledPlayerScoreWhenItWasKilledByTheWorld() {
    // Given a map with player register
    Map<String, String> players = new HashMap<>();
    players.put("2", "Isgalamido");
    // And a map with death register and a register with value 12
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Isgalamido", Integer.valueOf(12));
    // And a text with game events
    String text = " 21:42 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT\n";

    // When is searched a killer code and a killed player code in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel())).thenReturn("1022");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
        .thenReturn("2");
    // And is tried to collect negative score to the killed player because the killer is the world
    playerManager.collectScore(players, kills, text);

    // Then is decreased the killed player score
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(11));
  }

  @Test
  public void decreaseTheKillerScoreWhenTheKillerAndTheKilledPlayerAreTheSamePerson() {
    // Given a map with player register
    Map<String, String> players = new HashMap<>();
    players.put("2", "Isgalamido");
    // And a map with death register and a register with value 12
    Map<String, Integer> kills = new HashMap<>();
    kills.put("Isgalamido", Integer.valueOf(12));
    // And a text with game events
    String text = " 21:42 Kill: 2 2 22: Isgalamido killed Isgalamido by MOD_TRIGGER_HURT\n";

    // When is searched a killer code and a killed player code in the text
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLER_CODE.getLabel())).thenReturn("2");
    when(dataSearchEngine.find(text, PlayerRegex.TO_GET_KILLED_PLAYER_CODE.getLabel()))
        .thenReturn("2");
    // And is tried to collect negative score to the killer because the killer and the killed player are the same person
    playerManager.collectScore(players, kills, text);

    // Then is decreased the killer player score
    Assert.assertEquals(kills.get("Isgalamido"), Integer.valueOf(11));
  }
}
