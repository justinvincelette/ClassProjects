package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * Created by kevindo on 6/21/16. Represents a graphical view of a MusicEditorModel
 */
public interface GuiView extends MusicViewInterface {
  /**
   * Controls the up arrow key
   */
  void arrowUp();

  /**
   * Controls the down arrow key
   */
  void arrowDown();

  /**
   * Controls the left arrow key
   */
  void arrowLeft();

  /**
   * Controls the right arrow key
   */
  void arrowRight();

  /**
   * Controls the home button key
   */
  void jumpToStart();

  /**
   * Controls the end button key
   */
  void jumpToEnd();

  /**
   * Updates the GUI view
   *
   * @param currentBeat the current beat of the piece of music
   */
  void update(int currentBeat);

  /**
   * Controls the pause button of composition
   */
  void pause();

  /**
   * Controls the play button of composition
   */
  void play();

  /**
   * Adds a {@link KeyListener} to the GUI view
   *
   * @param k the {@link KeyListener} to add
   */
  void addKeyListener(KeyListener k);

  /**
   * Adds a {@link MouseListener} to the GUI view
   *
   * @param m the {@link MouseListener} to add
   */
  void addMouseListener(MouseListener m);

  /**
   * Removes a {@link MouseListener} from the GUI view
   *
   * @param m the {@link MouseListener} to remove
   */
  void removeMouseListener(MouseListener m);

  /**
   * Redraws the GUI notes, used after adding/removing/editing a note in the model
   *
   * @param model Model that was altered and needs it's notes to be redrawn
   */
  void editNotes(MusicInterface model);

  /**
   * Adds a note to the view.  Used only for composite view, where a note needs to be added to the
   * MIDI sequence
   *
   * @param note MusicNote to be added
   */
  void addNote(MusicNote note);

  /**
   * Removes a note from the view.  Used only for composite view, where a note needs to be removed
   * from the MIDI sequence
   *
   * @param model Model that has been changed after removing a MusicNote from it
   */
  void removeNote(MusicInterface model);
}




