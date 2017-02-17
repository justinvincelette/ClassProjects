package cs3500.music.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a MusicModel object, which controls how the music is represented and works
 */
public class MusicModel implements MusicInterface {
  private HashMap<Integer, ArrayList<MusicNote>> notes;
  private int numberOfBeats;
  MusicNote lowestNote;
  MusicNote highestNote;
  private int tempo;
  private int beatPerMeasure;

  /**
   * Constructs a MusicModel object, with default values.
   */
  public MusicModel() {
    notes = new HashMap<Integer, ArrayList<MusicNote>>();
    numberOfBeats = 0;
    lowestNote = new Note(0, 0, 0, 120, 0);
    highestNote = new Note(0, 0, 0, 0, 0);
    tempo = 0;
    beatPerMeasure = 0;
  }

  @Override
  public int getLength() {
    return numberOfBeats;
  }

  @Override
  public void addNote(MusicNote n) {
    if (n.compareTo(lowestNote) < 0) {
      lowestNote = n;
    }
    if (n.compareTo(highestNote) > 0) {
      highestNote = n;
    }
    if (numberOfBeats < n.getEnd() - 1) {
      numberOfBeats = n.getEnd() - 1;
    }

    Integer key = new Integer(n.getStart());
    ArrayList<MusicNote> notesAtBeat = notes.get(key);
    if (notesAtBeat == null) {
      notesAtBeat = new ArrayList<MusicNote>();
      notes.put(key, notesAtBeat);
    }
    notesAtBeat.add(n);
  }

  @Override
  public void removeNote(MusicNote n) {
    Integer noteKey = new Integer(n.getStart());
    ArrayList<MusicNote> list = notes.get(noteKey);
    if (list == null) {
      throw new IllegalArgumentException("Note does not exist");
    }
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(n)) {
        list.remove(i);
        return;
      }
    }
    throw new IllegalArgumentException("Note does not exist");
  }

  @Override
  public List<MusicNote> getNotesInBeat(int beatNum) {
    return notes.get(beatNum);
  }

  @Override
  public void setTempo(int tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Illegal tempo");
    }
    this.tempo = tempo;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setBeatPerMeasure(int beatPerMeasure) {
    if (beatPerMeasure <= 0) {
      throw new IllegalArgumentException("Invalid beat per measure");
    }
    this.beatPerMeasure = beatPerMeasure;
  }

  @Override
  public int getBeatPerMeasure() {
    return this.beatPerMeasure;
  }

  public MusicNote getLowestNote() {
    return new Note((Note) lowestNote);
  }

  public MusicNote getHighestNote() {
    return new Note((Note) highestNote);
  }

  public int getLowestNoteVal() {
    return lowestNote.getPitchOctaveNumber();
  }

  public int getHighestNoteVal() {
    return highestNote.getPitchOctaveNumber();
  }
}
