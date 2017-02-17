package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * Represents a Composite View, which combines a GUI and MIDI view to get a playable/editable music
 * sheet
 */
public class CompositeViewImpl implements MusicViewInterface, GuiView {
  private GuiViewFrame gui;
  private StartStopMidiView midi;
  private int currentBeat;
  private long currTick;
  private float tempo;

  /**
   * Constructs a CompositeViewImpl with the give GUI and Midi views
   */
  public CompositeViewImpl(GuiViewFrame gui, StartStopMidiView midi) {
    this.gui = gui;
    this.midi = midi;
    currentBeat = 0;
    currTick = 0;
  }

  @Override
  public void showView(MusicInterface model) {
    gui.showView(model);
    long previousTick = 0;
    currTick = 0;
    midi.showView(model);
    tempo = midi.getTempo();
    while (currTick < model.getLength()) {
      currTick = midi.getCurrentTick();
      if (currTick != previousTick) {
        currentBeat++;
        gui.update(currentBeat);
        previousTick = currTick;
      }
    }
  }

  @Override
  public void arrowUp() {
    gui.arrowUp();
  }

  @Override
  public void arrowDown() {
    gui.arrowDown();
  }

  @Override
  public void arrowLeft() {
    gui.arrowDown();
  }

  @Override
  public void arrowRight() {
    gui.arrowRight();
  }

  @Override
  public void jumpToStart() {
    currTick = 0;
    currentBeat = 0;
    midi.jumpToStart();
    gui.jumpToStart();
  }

  @Override
  public void jumpToEnd() {
    midi.jumpToEnd();
    gui.jumpToEnd();
  }

  @Override
  public void update(int currentBeat) {
    gui.update(currentBeat);
  }

  @Override
  public void pause() {
    this.midi.stop();
  }

  @Override
  public void play() {
    this.midi.start();
    this.midi.setTempo(tempo);
  }

  @Override
  public void addKeyListener(KeyListener k) {
    gui.addKeyListener(k);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    gui.addMouseListener(m);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    gui.removeMouseListener(m);
  }

  @Override
  public void editNotes(MusicInterface model) {
    gui.editNotes(model);
  }

  @Override
  public void addNote(MusicNote note) {
    midi.addNote(note);
  }

  @Override
  public void removeNote(MusicInterface model) {
    long tickPos = midi.getCurrentTick();
    midi.removeNote(model, tickPos);
  }
}
