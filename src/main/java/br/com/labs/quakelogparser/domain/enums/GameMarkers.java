package br.com.labs.quakelogparser.domain.enums;

public enum GameMarkers {

  KILL_MARKER("Kill"),
  INIT_GAME_MARKER("InitGame"),
  SHUT_DOWN_MARKER("------------------------------------------------------------"),
  NEW_PLAYER_MARKER("ClientUserinfoChanged"),
  WORLD_PLAYER_MARKER("<world>");

  private final String label;

  GameMarkers(final String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
