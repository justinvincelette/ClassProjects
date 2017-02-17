package cs3500.music.view;

import java.io.IOException;
import java.util.List;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;
import cs3500.music.model.Pitch;

/**
 * Represents a Console View, which shows the Music as text
 */
public class ConsoleViewImpl implements MusicViewInterface {
  private Appendable out;

  /**
   * Constructs a Console View, and will output the text to the given Appendable
   */
  public ConsoleViewImpl(Appendable out) {
    this.out = out;
  }

  @Override
  public void showView(MusicInterface model) {
    if (model.getLength() == 0) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    int maxBeat = model.getLength();
    sb.append(padRepresentation("", maxBeat));
    sb.append(noteRangeUsed(model));
    sb.append("\n");
    char[][] rep = notesAsText(model);
    for (int i = 0; i < rep.length; i++) {
      sb.append(padRepresentation(Integer.toString(i), maxBeat));
      for (int j = 0; j < rep[0].length; j++) {
        sb.append("  ");
        if (rep[i][j] == 0) {
          sb.append(" ");
        } else {
          sb.append(rep[i][j]);
        }
        sb.append("  ");
      }
      sb.append("\n");
    }
    try {
      out.append(sb.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Pads a string on the leftwith the correct number of spaces, based off the given number of max
   * beats
   *
   * @param s       String to pad
   * @param maxbeat maximum beats used in the model
   * @return the String that is now padded
   */
  private String padRepresentation(String s, int maxbeat) {
    int numberOfChars = Integer.toString(maxbeat).length();
    StringBuilder sb = new StringBuilder();
    int padding = numberOfChars - s.length();
    for (int i = 0; i < padding; i++) {
      sb.append(" ");
    }
    sb.append(s);
    return sb.toString();
  }

  /**
   * Determines the range of Note's that are used in the given model
   *
   * @param model Model that holds the notes
   * @return String containing all of the notes in the range used, formatted with the correct number
   * of spaces
   */
  private String noteRangeUsed(MusicInterface model) {
    StringBuilder sb = new StringBuilder();
    String Hnote = model.getHighestNote().toString();
    Pitch currPitch = model.getLowestNote().getPitch();
    int currOctave = model.getLowestNote().getOctave();
    String currNote = "";
    while (!currNote.equals(Hnote)) {
      currNote = currPitch.toString() + currOctave;
      if (currNote.length() == 2) {
        sb.append("  ").append(currNote).append(" ");
      } else if (currNote.length() == 3) {
        sb.append(" ").append(currNote).append(" ");
      } else if (currNote.length() == 4) {
        sb.append(" ").append(currNote);
      }
      currPitch = Pitch.values()[(currPitch.ordinal() + 1) % 12];
      if (currPitch == Pitch.C) {
        currOctave++;
      }
    }
    return sb.toString();
  }

  /**
   * Creates a two dimensional array of characters, which easily allows the entering of "X" where
   * Note's begin, and "|" for the rest of the Note's duration
   *
   * @param model Model that holds all of the notes
   * @return Two dimensional array of characters, with the notes in the appropriate text
   * representation
   */
  private char[][] notesAsText(MusicInterface model) {
    int highestNoteIndex = model.getHighestNote().getPitchOctaveNumber();
    int lowestNoteIndex = model.getLowestNote().getPitchOctaveNumber();
    int numOfNotes = highestNoteIndex - lowestNoteIndex + 1;
    int numOfBeats = model.getLength() + 1;
    char[][] chars = new char[numOfBeats][numOfNotes];
    Integer key = 0;
    List<MusicNote> notesAtBeat;
    while (key <= numOfBeats) {
      notesAtBeat = model.getNotesInBeat(key);
      if (notesAtBeat != null) {
        for (MusicNote n : notesAtBeat) {
          int currentBeat = key;
          int lastBeat = n.getEnd();
          int location = n.getPitchOctaveNumber() - lowestNoteIndex;
          chars[currentBeat][location] = 'X';
          currentBeat++;
          while (currentBeat < lastBeat) {
            chars[currentBeat][location] = '|';
            currentBeat++;
          }
        }
      }
      key++;
    }
    return chars;
  }
}
