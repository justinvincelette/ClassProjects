package cs3500.music.view;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * Enforces the methods that a StartStop View should have. StartStop includes ability to pause/play,
 * move current beat, add and remove notes
 */
public interface StartStopViewInterface extends MusicViewInterface {
  /**
   * Starts playing the MIDI sequence
   */
  void start();

  /**
   * Stops playing the MIDI sequence
   */
  void stop();

  /**
   * Obtains the beat where the Sequence is currently at
   *
   * @return long number representing beat/tick
   */
  long getCurrentTick();

  /**
   * Obtains the tempo of the Sequence
   *
   * @return tempo
   */
  float getTempo();

  /**
   * Sets the tempo of the Sequence
   *
   * @param tempo long number representing what to set the tempo to be
   */
  void setTempo(float tempo);

  /**
   * Adds a MusicNote to the Sequence
   *
   * @param n MusicNote to add
   */
  void addNote(MusicNote n);

  /**
   * Removes a MusicNote from the Sequence, by constructing a new sequence without a Note
   *
   * @param model   new Model after removing the MusicNote
   * @param tickPos current position of the Sequence
   */
  void removeNote(MusicInterface model, long tickPos);

  /**
   * Moves the Sequence tick position to 0, in order to go to beginning of song
   */
  void jumpToStart();

  /**
   * Moves the Sequence tick position to the end, in order to go to end of song
   */
  void jumpToEnd();
}
