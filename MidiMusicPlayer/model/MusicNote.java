package cs3500.music.model;

/**
 * Interface that enforces what methods a Note must implement
 */
public interface MusicNote extends Comparable<MusicNote> {
  /**
   * Gets the Pitch for this MusicNote
   *
   * @return enum type representing the Pitch
   */
  Pitch getPitch();

  /**
   * Gets the octave for this MusicNote
   *
   * @return int representing the octave
   */
  int getOctave();

  /**
   * Gets the starting beat for this MusicNote
   *
   * @return int representing starting beat
   */
  int getStart();

  /**
   * Gets the ending beat for this MusicNote
   *
   * @return int representing beat where note ends
   */
  int getEnd();

  /**
   * Gets the instrument for this MusicNote
   *
   * @return int representing the instrument type
   */
  int getInstrument();

  /**
   * Gets the volume for this MusicNote
   *
   * @return int representing the volume
   */
  int getVolume();

  /**
   * Sets this MusicNote's Pitch
   *
   * @param p Pitch to set
   */
  void setPitch(Pitch p);

  /**
   * Sets this MusicNote's octave
   *
   * @param o octave to set
   */
  void setOctave(int o);

  /**
   * Sets this MusicNotes's starting beat
   *
   * @param s int for starting beat
   */
  void setStart(int s);

  /**
   * Sets this MusicNote's ending beat
   *
   * @param e int for ending beat
   */
  void setEnd(int e);

  /**
   * Sets this MusicNote's instrument
   *
   * @param i int for instrument
   */
  void setInstrument(int i);

  /**
   * Sets this MusicNote's volume
   *
   * @param v int for volume
   */
  void setVolume(int v);

  /**
   * Gets the int represention of this MusicNote's pitch/octave combo. (Based off C4 being 60)
   *
   * @return int representation
   */
  int getPitchOctaveNumber();
}
