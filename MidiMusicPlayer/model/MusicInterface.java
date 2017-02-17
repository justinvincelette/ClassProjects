package cs3500.music.model;

import java.util.List;

/**
 * Interface that enforces Music Model methods
 */
public interface MusicInterface {
  /**
   * Returns the length of piece as a number of beats
   *
   * @return the length of this Model
   */
  int getLength();

  /**
   * Adds a copy of given Note
   *
   * @param n the new Note to be added
   */
  void addNote(MusicNote n);

  /**
   * Removes the Note that is exactly the given Note from this Model
   *
   * @param n Note to be removed
   * @throws IllegalArgumentException if the given Note is not in model
   */
  void removeNote(MusicNote n);

  /**
   * Returns a list of all Notes that are playing at the given beat number
   *
   * @return List of all Notes
   */
  List<MusicNote> getNotesInBeat(int beatNum);

  /**
   * Sets the tempo of this Model to the given value
   *
   * @param tempo the tempo to be set
   * @throws IllegalArgumentException if the tempo is not positive
   */
  void setTempo(int tempo);

  /**
   * Gets the tempo of this Model
   *
   * @return the tempo of this Model in beats per minute
   */
  int getTempo();

  /**
   * Sets the number of beats in one measure
   *
   * @throws IllegalArgumentException if the given beats per measure is not positive
   */
  void setBeatPerMeasure(int beatPerMeasure);

  /**
   * Gets the number of beats in one measure
   *
   * @return the number of beats in one measure
   */
  int getBeatPerMeasure();

  /**
   * Gets the lowest note that is in the Model
   *
   * @return the lowest note
   */
  MusicNote getLowestNote();

  /**
   * Gets the highest note that is in the Model
   */
  MusicNote getHighestNote();

  /**
   * Gets the int representation for the lowest note in this Model. (based off C4 being 60)
   *
   * @return int representation
   */
  int getLowestNoteVal();

  /**
   * Gets the int representation highest note in this Model. (based off C4 being 60)
   *
   * @return int representation
   */
  int getHighestNoteVal();
}
