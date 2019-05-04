package br.com.labs.quakelogparser.domain.enumeration;

public enum PlayerRegex {

  TO_GET_PLAYER_CODE("ClientUserinfoChanged\\:\\s(\\d+)"),
  TO_GET_PLAYER_NAME("\\sn\\\\([\\w\\W]+)\\\\t\\\\"),
  TO_GET_KILLER_CODE("Kill:\\s(\\d+)"),
  TO_GET_KILLED_PLAYER_CODE("Kill:\\s\\d+\\s(\\d+)");

  private final String label;

  PlayerRegex(final String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
