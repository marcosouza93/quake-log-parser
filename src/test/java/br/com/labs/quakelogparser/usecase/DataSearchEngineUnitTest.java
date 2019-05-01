package br.com.labs.quakelogparser.usecase;

import br.com.labs.quakelogparser.domain.enums.PlayerRegex;
import br.com.labs.quakelogparser.usecase.exception.DataNotFoundByRegexException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

public class DataSearchEngineUnitTest {

  @InjectMocks
  DataSearchEngine dataSearchEngine;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findPlayerCodeWithSmallCode() {
    // Given a text with a player code information and an expression to find a player code
    String text =
        " 20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
    String regex = PlayerRegex.TO_GET_PLAYER_CODE.label();

    // When is searched a player code
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a player code
    Assert.assertEquals("2", result);
  }

  @Test
  public void findPlayerCodeWithBigCode() {
    // Given a text with a big player code information and an expression to find a player code
    String text =
        " 20:34 ClientUserinfoChanged: 12345678910 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
    String regex = PlayerRegex.TO_GET_PLAYER_CODE.label();

    // When is searched a player code
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a player code
    Assert.assertEquals("12345678910", result);
  }

  @Test
  public void findPlayerName() {
    // Given a text with a player name information and an expression to find a player name
    String text =
        " 20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
    String regex = PlayerRegex.TO_GET_PLAYER_NAME.label();

    // When is searched a player name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a player name
    Assert.assertEquals("Isgalamido", result);
  }

  @Test
  public void findPlayerNameWithMultipleWords() {
    // Given a text with a player name information using multiple words and an expression to find a player name
    String text =
        "  21:51 ClientUserinfoChanged: 3 n\\Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje\\t\\0\\model\\sarge/krusade\\hmodel\\sarge/krusade\\g_redteam\\\\g_blueteam\\\\c1\\5\\c2\\5\\hc\\95\\w\\0\\l\\0\\tt\\0\\tl\\0\n";
    String regex = PlayerRegex.TO_GET_PLAYER_NAME.label();

    // When is searched a player name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a player name
    Assert.assertEquals(
        "Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje", result);
  }

  @Test
  public void findKillerName() {
    // Given a text with a killer name information and an expression to find a killer name
    String text = " 22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH\n";
    String regex = PlayerRegex.TO_GET_KILLER.label();

    // When is searched a killer name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a killer name
    Assert.assertEquals("Isgalamido", result);
  }

  @Test
  public void findKillerNameWithMultipleWords() {
    // Given a text with a killer name information using multiple words and an expression to find a killer name
    String text =
        " 22:06 Kill: 2 3 7: Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje killed Mocinha by MOD_ROCKET_SPLASH\n";
    String regex = PlayerRegex.TO_GET_KILLER.label();

    // When is searched a killer name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a killer name
    Assert.assertEquals(
        "Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje", result);
  }

  @Test
  public void findKillerNameWhenItIsTheWorld() {
    // Given a text with the world player and an expression to find a killer name
    String text = " 21:42 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT\n";
    String regex = PlayerRegex.TO_GET_KILLER.label();

    // When is searched a killer name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly the world player as the killer
    Assert.assertEquals("<world>", result);
  }

  @Test
  public void findKilledPlayerName() {
    // Given a text with a killed player name and an expression to find a killed player name
    String text = " 22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH\n";
    String regex = PlayerRegex.TO_GET_KILLED_PLAYER.label();

    // When is searched a killed player name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a killed player name
    Assert.assertEquals("Mocinha", result);
  }

  @Test
  public void findKilledPlayerNameWithMultipleWords() {
    // Given a text with a killed player name using multiple words and an expression to find a killed player name
    String text =
        " 22:06 Kill: 2 3 7: Isgalamido killed Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje by MOD_ROCKET_SPLASH\n";
    String regex = PlayerRegex.TO_GET_KILLED_PLAYER.label();

    // When is searched a killed player name
    String result = dataSearchEngine.find(text, regex);

    // Then is returned correctly a killed player name
    Assert.assertEquals(
        "Dono da Bola de futebol mais antiga do mundo vista até os dias de hoje", result);
  }

  @Test(expected = DataNotFoundByRegexException.class)
  public void NotFindDataWhenIsUsedIncorrectExpression() {
    // Given a text with only death information and an expression to find player information
    String text = " 22:06 Kill: 2 3 7: Isgalamido killed Mocinha by MOD_ROCKET_SPLASH\n";
    String regex = PlayerRegex.TO_GET_PLAYER_NAME.label();

    try {
      // When is searched a player information in a text with different purpose
      dataSearchEngine.find(text, regex);
    } catch (DataNotFoundByRegexException ex) {
      // Then is returned an error about data not found
      Assert.assertEquals(HttpStatus.NOT_FOUND, ex.httpStatus());
      throw ex;
    }
  }
}
