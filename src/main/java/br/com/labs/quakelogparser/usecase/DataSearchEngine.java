package br.com.labs.quakelogparser.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DataSearchEngine {

  /**
   * Searches for a data through a expression
   * @param text
   *        The text base used to search a data
   * @param regex
   *        The expression to be compiled
   * @return The result found as {@link String}
   */
  public String find(String text, String regex) {
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(text);
    matcher.find();

    return matcher.group(1);
  }
}
