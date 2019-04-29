package br.com.labs.quakelogparser.usecase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DataSearchEngine {

  public String find(String text, String regex) {
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(text);
    matcher.find();

    return matcher.group(1);
  }
}
