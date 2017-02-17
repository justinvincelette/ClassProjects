package cs3500.music.model;

/**
 * Enumeration representing the different Pitches for a note
 */
public enum Pitch {
  C("C"), CSharp("C#"), D("D"), DSharp("D#"), E("E"), F("F"), FSharp("F#"), G("G"), GSharp("G#"),
  A("A"), ASharp("A#"), B("B");

  private String representation;

  Pitch(String rep) {
    representation = rep;
  }

  /**
   * Returns string representation of this Pitch
   *
   * @return String
   */
  public String toString() {
    return representation;
  }
}
