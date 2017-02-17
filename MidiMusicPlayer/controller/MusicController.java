package cs3500.music.controller;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Note;
import cs3500.music.view.CompositeViewImpl;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewFrame;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * Created by kevindo on 6/21/16. Controller for the view of the editor
 */
public class MusicController {
  private MusicInterface model;
  private GuiView view;
  private int currentBeat;
  private boolean isEditMode = false, isRemoveMode = false;
  private boolean isPaused = false, isAddingNote = false, isEditingNote = false;
  private MusicNote AddableNote, currentEditableNote;

  /**
   * Initializes this {@link MusicController} to a {@link MusicInterface} and {@link GuiView}. Then
   * adds both the {@code keyboardHandler} and the {@code mouseHandler} for GUI control
   *
   * @param model the {@link MusicInterface}
   * @param view  the {@link GuiView}
   */
  public MusicController(MusicInterface model, GuiView view) {
    this.model = model;
    this.view = view;
    this.view.addKeyListener(this.keyboardHandler);
    this.view.addMouseListener(this.mouseHandler);
    this.view.showView(this.model);
  }

  /**
   * Gets the {@link MusicNote} that was clicked in composition
   *
   * @param beat            the beat of the {@link MusicNote} clicked
   * @param noteNumberValue the note's numerical value [0 - 127] of the {@link MusicNote} clicked
   * @return the {@link MusicNote} that was clicked
   */
  private MusicNote getClickedNote(int beat, int noteNumberValue) {
    List<MusicNote> notes = this.model.getNotesInBeat(beat);
    if (notes != null) {
      for (MusicNote p : notes) {
        if ((p.getStart() == beat) && (p.getPitchOctaveNumber() == noteNumberValue)) {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * A Runnable lambda for adding a note to the composition
   */
  private final Runnable addNote = () -> {
    if (this.isPaused) {
      this.isAddingNote = !this.isAddingNote;

      int clickedYCoordinate = this.mouseHandler.getCurrentMouseEvent().getY();

      int highestNoteVal = this.model.getHighestNoteVal();
      int pitch = (clickedYCoordinate / GuiViewFrame.BLOCK) - 1;
      int newNoteVal = highestNoteVal - pitch;

      int start = (this.mouseHandler.getCurrentMouseEvent().getX() / GuiViewFrame.BLOCK) - 1;

      int xPos = this.mouseHandler.getCurrentMouseEvent().getX() / GuiViewFrame.BLOCK;

      if (clickedYCoordinate > GuiViewFrame.BLOCK
              && clickedYCoordinate <= (this.model.getHighestNoteVal() -
              this.model.getLowestNoteVal() + 2)
              * GuiViewFrame.BLOCK
              && xPos <= this.model.getLength()) {
        if (this.isAddingNote) {
          this.AddableNote = new Note(start, 100, 1, newNoteVal, 100);
        } else if (xPos - this.AddableNote.getStart() > 0) {
          this.AddableNote.setEnd(xPos);
          this.model.addNote(this.AddableNote);
          this.view.editNotes(model);
          if (this.view instanceof CompositeViewImpl) {
            this.view.addNote(this.AddableNote);
          }
        }
      }
    }
  };

  /**
   * Runnable lambda for toggling edit mode after pressing the "Z" key
   */
  private final Runnable editMode = () -> {
    this.isRemoveMode = false;
    /* Toggling edit mode will stop remove mode */
    this.isEditMode = !this.isEditMode;

    /* Allows mouse commands for editing/dragging notes after de-referencing remove mode mouse
     * commands */
    if (this.isEditMode) {
      this.view.removeMouseListener(this.mouseHandlerRemoveMode);
      this.view.removeMouseListener(this.mouseHandler);
      this.view.addMouseListener(this.mouseHandlerEditMode);
    } else {
      this.view.removeMouseListener(this.mouseHandlerEditMode);
      this.view.addMouseListener(this.mouseHandler);
    }
  };

  /**
   * A Runnable lambda for editing notes in a composition
   */
  private final Runnable editNote = () -> {
    if (this.isPaused) {
      this.isEditingNote = !this.isEditingNote;

      int beat = (this.mouseHandlerEditMode.getCurrentMouseEvent().getX() /
              GuiViewFrame.BLOCK) - 1;
      int noteValue = (-1 * this.mouseHandlerEditMode.getCurrentMouseEvent().getY()) /
              GuiViewFrame.BLOCK + this.model.getHighestNoteVal() + 1;

      if (this.isEditingNote) {
        this.currentEditableNote = this.getClickedNote(beat, noteValue);
        if (this.currentBeat < this.model.getLength() && this.currentEditableNote != null) {
          this.model.removeNote(this.currentEditableNote);
          if (this.view instanceof CompositeViewImpl) {
            this.view.removeNote(this.model);
          }
        }
      } else {
        if (this.currentBeat < this.model.getLength() && this.currentEditableNote != null) {
          Note newNote = new Note(beat, this.currentEditableNote.getEnd(),
                  this.currentEditableNote.getInstrument(), noteValue,
                  this.currentEditableNote.getVolume());
          this.model.addNote(newNote);
          this.view.editNotes(this.model);
          if (this.view instanceof CompositeViewImpl) {
            this.view.addNote(newNote);
          }
        }
      }
    }
  };

  /**
   * Runnable lambda for toggling remove mode after pressing the "X" key
   */
  private final Runnable removeMode = () -> {
    this.isEditMode = false;
    /* Toggling remove mode will stop edit mode */
    this.isRemoveMode = !this.isRemoveMode;

    /* Allows mouse commands for removing notes after de-referencing edit mode mouse commands */
    if (this.isRemoveMode) {
      this.view.removeMouseListener(this.mouseHandlerEditMode);
      this.view.removeMouseListener(this.mouseHandler);
      this.view.addMouseListener(this.mouseHandlerRemoveMode);
    } else {
      this.view.removeMouseListener(this.mouseHandlerRemoveMode);
      this.view.addMouseListener(this.mouseHandler);
    }
  };

  /**
   * A Runnable lambda for removing notes from the composition
   */
  private final Runnable removeNote = () -> {
    if (this.isPaused) {
      int beat = (this.mouseHandlerRemoveMode.getCurrentMouseEvent().getX() /
              GuiViewFrame.BLOCK) - 1;

      int noteValue = (-1 * this.mouseHandlerRemoveMode.getCurrentMouseEvent().getY()) /
              GuiViewFrame.BLOCK + this.model.getHighestNoteVal() + 1;

      MusicNote n = this.getClickedNote(beat, noteValue);

      if (n != null && this.currentBeat < this.model.getLength()) {
        this.model.removeNote(n);
        this.view.editNotes(model);
        if (this.view instanceof CompositeViewImpl) {
          this.view.removeNote(this.model);
        }
      }
    }
  };

  /**
   * Runnable lambda method for playing and pausing a composition using the space key
   */
  private final Runnable playPauseSong = () -> {
    if (view instanceof CompositeViewImpl) {
      if (this.isPaused) {
        this.view.play();
      } else {
        this.view.pause();
      }
      this.isPaused = !this.isPaused;
    }
  };

  /**
   * Runnable lambda method for scrolling up a composition using directional arrow keys
   */
  private final Runnable arrowUp = () -> this.view.arrowUp();
  private final Runnable arrowDown = () -> this.view.arrowDown();
  private final Runnable arrowLeft = () -> this.view.arrowLeft();
  private final Runnable arrowRight = () -> this.view.arrowRight();

  /**
   * Runnable lambda method for jumping to the start of a composition using the Home key
   */
  private final Runnable jumpToStart = () -> this.view.jumpToStart();

  /**
   * Runnable lambda method for jumping to the end of a composition using the End key
   */
  private final Runnable jumpToEnd = () -> this.view.jumpToEnd();

  /**
   * Initialize {@link KeyboardHandler} for handling input from the keyboard
   */
  private final KeyboardHandler keyboardHandler = new KeyboardHandler.Builder()
          .addKeyPressed(KeyEvent.VK_Z, this.editMode)
          .addKeyPressed(KeyEvent.VK_X, this.removeMode)
          .addKeyReleased(KeyEvent.VK_SPACE, this.playPauseSong)
          .addKeyPressed(KeyEvent.VK_UP, this.arrowUp)
          .addKeyPressed(KeyEvent.VK_DOWN, this.arrowDown)
          .addKeyPressed(KeyEvent.VK_RIGHT, this.arrowRight)
          .addKeyPressed(KeyEvent.VK_LEFT, this.arrowLeft)
          .addKeyPressed(KeyEvent.VK_HOME, this.jumpToStart)
          .addKeyPressed(KeyEvent.VK_END, this.jumpToEnd)
          .build();

  /**
   * Initialize {@link MouseHandler} for handling input from the mouse (Used primarily for adding
   * notes)
   */
  private final MouseHandler mouseHandler = new MouseHandler.Builder()
          .addMousePressed(MouseEvent.BUTTON1, this.addNote)
          .addMouseReleased(MouseEvent.BUTTON1, this.addNote)
          .build();

  /**
   * Initialize {@link MouseHandler} for handling input from the mouse (Used primarily for editing
   * notes)
   */
  private final MouseHandler mouseHandlerEditMode = new MouseHandler.Builder()
          .addMousePressed(MouseEvent.BUTTON1, this.editNote)
          .addMouseReleased(MouseEvent.BUTTON1, this.editNote)
          .build();

  /**
   * Initialize {@link MouseHandler} for handling input from the mouse (Used primarily for removing
   * notes)
   */
  private final MouseHandler mouseHandlerRemoveMode = new MouseHandler.Builder()
          .addMousePressed(MouseEvent.BUTTON1, this.removeNote)
          .build();
}
