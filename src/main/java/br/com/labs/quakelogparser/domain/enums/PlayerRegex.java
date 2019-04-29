package br.com.labs.quakelogparser.domain.enums;

public enum PlayerRegex {
  USER_CODE("ClientUserinfoChanged\\:\\s(\\d)"),
  USER_NAME("\\sn\\\\([\\w\\W]+)\\\\t\\\\"),
  KILLER("\\d:\\s([\\w\\W]+)\\skilled"),
  KILLED("killed\\s([\\w\\W]+)\\sby");

  private final String label;

  PlayerRegex(final String label) {
    this.label = label;
  }

  public String label() {
    return this.label;
  }
}
