package br.com.labs.quakelogparser.domain.enums;

public enum PlayerRegex {
  TO_GET_PLAYER_CODE("ClientUserinfoChanged\\:\\s(\\d)"),
  TO_GET_PLAYER_NAME("\\sn\\\\([\\w\\W]+)\\\\t\\\\"),
  TO_GET_KILLER("\\d:\\s([\\w\\W]+)\\skilled"),
  TO_GET_KILLED_PLAYER("killed\\s([\\w\\W]+)\\sby");

  private final String label;

  PlayerRegex(final String label) {
    this.label = label;
  }

  public String label() {
    return this.label;
  }
}
