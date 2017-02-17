package cs3500.music.view;

import cs3500.music.controller.MouseHandler;
import cs3500.music.controller.MusicController;
import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Pitch;
import cs3500.music.view.GuiView;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; // Possibly of interest for handling mouse events

import javax.swing.*;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public final class GuiViewFrame extends JFrame implements GuiView {

  private final JFrame frame = new JFrame("CS 3500 - Kevin and Justin's Music Editor");
  private final JPanel displayPanel = new JPanel(new BorderLayout());
  private final JScrollPane scroll = new JScrollPane(displayPanel);
  private MusicInterface mem;
  static int currentBeat;
  public static final int BLOCK = 20;
  private ConcreteGuiViewPanel panel;

  /**
   * Initialize the GUI view
   */
  private void init() {
    this.panel = new ConcreteGuiViewPanel(this.mem);
    Box notePanel = drawPitches();
    Box beatPanel = drawBeats();

    this.displayPanel.add(notePanel, BorderLayout.WEST);
    this.displayPanel.add(beatPanel, BorderLayout.NORTH);
    this.displayPanel.add(panel, BorderLayout.CENTER);

    this.scroll.getVerticalScrollBar().setUnitIncrement(16);
    this.scroll.getHorizontalScrollBar().setUnitIncrement(16);

    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.setPreferredSize(new Dimension(
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), 500));
    this.frame.setBackground(Color.LIGHT_GRAY);
    this.frame.add(scroll);
    this.frame.pack();
    this.frame.setVisible(true);
  }

  /**
   * Draw pitches
   *
   * @return a vertical box with pitches values
   */
  private Box drawPitches() {
    Box pitch = Box.createVerticalBox();
    Pitch currPitch = mem.getHighestNote().getPitch();
    int currOctave = mem.getHighestNote().getOctave();
    int pitchOrdinal = currPitch.ordinal();
    String currNote;

    for (int i = mem.getHighestNoteVal(); i >= mem.getLowestNoteVal(); i--) {
      currNote = currPitch.toString() + currOctave;

      JLabel label = new JLabel(currNote);
      pitch.add(Box.createVerticalStrut(BLOCK / 5));
      pitch.add(label);

      pitchOrdinal--;
      if (pitchOrdinal < 0) {
        pitchOrdinal = 11;
      }
      currPitch = Pitch.values()[pitchOrdinal];
      if (currPitch == Pitch.B) {
        currOctave--;
      }
    }
    return pitch;
  }

  /**
   * Draw times
   *
   * @return a horizontal box with beat values
   */
  private Box drawBeats() {
    Box beat = Box.createHorizontalBox();
    beat.add(Box.createHorizontalStrut((BLOCK * 2) - 16));
    for (int i = 0; i <= mem.getLength(); i++) {
      if (i % 16 == 0) {
        Box container = Box.createHorizontalBox();
        container.setMinimumSize(new Dimension(BLOCK * 4, BLOCK));
        container.setMaximumSize(new Dimension(BLOCK * 4, BLOCK));
        container.setPreferredSize(new Dimension(BLOCK * 4, BLOCK));
        JLabel label = new JLabel(Integer.toString(i));
        container.add(label);
        beat.add(container);
        i += 3;
      } else {
        beat.add(Box.createHorizontalStrut(BLOCK));
      }
    }
    return beat;
  }

  /**
   * Shows/Outputs the appropriate view from the given model
   */
  @Override
  public void showView(MusicInterface model) {
    this.mem = model;
    this.init();
  }

  /**
   * Controls the up arrow key
   */
  @Override
  public void arrowUp() {
    this.scroll.getVerticalScrollBar().setValue(this.scroll.getVerticalScrollBar().getValue() -
            this.scroll.getVerticalScrollBar().getUnitIncrement());
  }

  /**
   * Controls the down arrow key
   */
  @Override
  public void arrowDown() {
    this.scroll.getVerticalScrollBar().setValue(this.scroll.getVerticalScrollBar().getValue() +
            this.scroll.getVerticalScrollBar().getUnitIncrement());
  }

  /**
   * Controls the left arrow key
   */
  @Override
  public void arrowLeft() {
    this.scroll.getHorizontalScrollBar().setValue(this.scroll.getVerticalScrollBar().getValue() -
            this.scroll.getHorizontalScrollBar().getUnitIncrement());
  }

  /**
   * Controls the right arrow key
   */
  @Override
  public void arrowRight() {
    this.scroll.getHorizontalScrollBar().setValue(this.scroll.getVerticalScrollBar().getValue() +
            this.scroll.getHorizontalScrollBar().getUnitIncrement());
  }

  /**
   * Controls the home button key
   */
  @Override
  public void jumpToStart() {
    this.scroll.getHorizontalScrollBar().setValue(0);
    this.update(0);
  }

  /**
   * Controls the end button key
   */
  @Override
  public void jumpToEnd() {
    this.update(this.mem.getLength());
  }

  /**
   * Updates the GUI view
   *
   * @param currentBeat the current beat of the piece of music
   */
  @Override
  public void update(int currentBeat) {
    GuiViewFrame.currentBeat = currentBeat;
    int beat = GuiViewFrame.currentBeat;
    this.displayPanel.repaint();
    if (beat * GuiViewFrame.BLOCK >= this.frame.getBounds().getSize().getWidth()
            - GuiViewFrame.BLOCK) {
      this.scroll.getHorizontalScrollBar().setValue(currentBeat * GuiViewFrame.BLOCK);
    }
  }

  /**
   * Controls the pause button of composition
   */
  @Override
  public void pause() {
    throw new IllegalArgumentException("Not supported for a regular GuiViewFrame");
  }

  /**
   * Controls the play button of composition
   */
  @Override
  public void play() {
    throw new IllegalArgumentException("Not supported for a regular GuiViewFrame");
  }

  /**
   * Adds a {@link KeyListener} to the GUI view
   *
   * @param k the {@link KeyListener} to add
   */
  @Override
  public void addKeyListener(KeyListener k) {
    this.frame.addKeyListener(k);
  }

  /**
   * Adds a {@link MouseListener} to the GUI view
   *
   * @param m the {@link MouseListener} to add
   */
  @Override
  public void addMouseListener(MouseListener m) {
    this.displayPanel.addMouseListener(m);
  }

  /**
   * Removes a {@link MouseListener} from the GUI view
   *
   * @param m the {@link MouseListener} to remove
   */
  @Override
  public void removeMouseListener(MouseListener m) {
    this.displayPanel.removeMouseListener(m);
  }

  @Override
  public void editNotes(MusicInterface model) {
    this.mem = model;
    this.displayPanel.removeAll();
    this.init();
  }

  @Override
  public void addNote(MusicNote note) {
    throw new IllegalArgumentException("Not supported for a regular GuiViewFrame");
  }

  @Override
  public void removeNote(MusicInterface model) {
    throw new IllegalArgumentException("Not supported for a regular GuiViewFrame");
  }
}
