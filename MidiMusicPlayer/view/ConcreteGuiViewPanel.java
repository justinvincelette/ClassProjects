package cs3500.music.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * A dummy view that simply draws a string
 */
public class ConcreteGuiViewPanel extends JPanel {
  private final MusicInterface mem;

  /**
   * Constructor for {@link ConcreteGuiViewPanel}
   */
  public ConcreteGuiViewPanel(MusicInterface mem) {
    this.mem = mem;
  }

  @Override
  public void paintComponent(Graphics g) {
    // Handle the default painting
    // Look for more documentation about the Graphics class,
    // and methods on it that may be useful
    this.paintAllNotes(g);
    this.paintGrid(g);
    this.paintCurrentBeatLine(GuiViewFrame.currentBeat, g);
  }

  /**
   * Draws the current beat red line
   *
   * @param currentBeat the current beat of the piece of music
   * @param g           graphics
   */
  private void paintCurrentBeatLine(int currentBeat, Graphics g) {
    g.setColor(Color.RED);
    g.drawLine(currentBeat * GuiViewFrame.BLOCK, 0, currentBeat * GuiViewFrame.BLOCK,
            (this.mem.getHighestNoteVal() - this.mem.getLowestNoteVal() + 1) * GuiViewFrame.BLOCK);
  }

  /**
   * Draws the grid
   *
   * @param g graphics
   */
  private void paintGrid(Graphics g) {
    g.setColor(Color.BLACK);
    for (int i = 0; i <= this.mem.getHighestNoteVal() - this.mem.getLowestNoteVal() + 1; i++) {
      ((Graphics2D) g).setStroke(new BasicStroke(2));
      if ((this.mem.getHighestNoteVal() - i + 1) % 12 == 0) {
        ((Graphics2D) g).setStroke(new BasicStroke(3));
      }
      g.drawLine(0, i * GuiViewFrame.BLOCK, this.mem.getLength() * GuiViewFrame.BLOCK,
              i * GuiViewFrame.BLOCK);
    }

    for (int i = 0; i <= this.mem.getLength(); i++) {
      if (i % 4 == 0 | i == this.mem.getLength()) {
        g.drawLine(i * GuiViewFrame.BLOCK, 0, i * GuiViewFrame.BLOCK,
                (this.mem.getHighestNoteVal() - this.mem.getLowestNoteVal() + 1)
                        * GuiViewFrame.BLOCK);
      }
    }
  }

  /**
   * Draws all the {@link MusicNote}s in the composition
   *
   * @param g graphics
   */
  private void paintAllNotes(Graphics g) {
    for (int i = 0; i < this.mem.getLength(); i++) {
      List<MusicNote> notesAtBeat = this.mem.getNotesInBeat(i);
      if (notesAtBeat != null) {
        for (MusicNote n : notesAtBeat) {
          if (n.getStart() == i) {
            this.paintNote(n, g);
          }
        }
      }
    }
  }

  /**
   * Draws a {@link MusicNote}
   *
   * @param n the {@link MusicNote} to draw
   * @param g graphics
   */
  private void paintNote(MusicNote n, Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(n.getStart() * GuiViewFrame.BLOCK,
            (this.mem.getHighestNoteVal() - n.getPitchOctaveNumber()) * GuiViewFrame.BLOCK,
            GuiViewFrame.BLOCK, GuiViewFrame.BLOCK);
    g.setColor(Color.GREEN);
    int end = (n.getEnd() - 1) - n.getStart();
    g.fillRect((n.getStart() + 1) * GuiViewFrame.BLOCK,
            (this.mem.getHighestNoteVal() - n.getPitchOctaveNumber()) * GuiViewFrame.BLOCK,
            end * GuiViewFrame.BLOCK, GuiViewFrame.BLOCK);
  }

}
