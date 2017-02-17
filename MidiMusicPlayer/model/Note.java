package cs3500.music.model;

/**
 * Represents a Note object, which contains a Pitch, octave number, starting/ending beats,
 * instrument, and volume
 */
public class Note implements MusicNote {
  private int start;

  private int end;

  private int instrument;

  private Pitch pitch;

  private int octave;

  private int volume;

  /**
   * Constructs a Note object using a MIDI pitch number and other values
   *
   * @param start      starting beat
   * @param end        ending beat
   * @param instrument instrument number
   * @param MidiPitch  MIDI pitch representation (C4 is 60)
   * @param volume     volume number
   */
  public Note(int start, int end, int instrument, int MidiPitch, int volume) {
    this.start = start;
    this.end = end;
    this.instrument = instrument;
    this.volume = volume;
    int pitchNum = MidiPitch % 12;
    this.octave = MidiPitch / 12 - 1;
    switch (pitchNum) {
      case 0:
        this.pitch = Pitch.C;
        break;
      case 1:
        this.pitch = Pitch.CSharp;
        break;
      case 2:
        this.pitch = Pitch.D;
        break;
      case 3:
        this.pitch = Pitch.DSharp;
        break;
      case 4:
        this.pitch = Pitch.E;
        break;
      case 5:
        this.pitch = Pitch.F;
        break;
      case 6:
        this.pitch = Pitch.FSharp;
        break;
      case 7:
        this.pitch = Pitch.G;
        break;
      case 8:
        this.pitch = Pitch.GSharp;
        break;
      case 9:
        this.pitch = Pitch.A;
        break;
      case 10:
        this.pitch = Pitch.ASharp;
        break;
      case 11:
        this.pitch = Pitch.B;
        break;
      default:
        throw new IllegalArgumentException("Note has an invalid pitch");
    }
  }

  /**
   * Constructs a Note by getting passed variables that directly relate to this Note's private
   * variables
   *
   * @param start      starting beat
   * @param end        ending beat
   * @param instrument instrument number
   * @param pitch      Pitch enum type
   * @param octave     octave number
   * @param volume     volume number
   */
  public Note(int start, int end, int instrument, Pitch pitch, int octave, int volume) {
    this.start = start;
    this.end = end;
    this.instrument = instrument;
    this.pitch = pitch;
    this.octave = octave;
    this.volume = volume;
  }

  /**
   * Constructs a Note by copying another Note
   *
   * @param other Note to copy
   */
  public Note(Note other) {
    if (other == null) {
      return;
    }
    this.start = other.start;
    this.end = other.end;
    this.instrument = other.instrument;
    this.pitch = other.pitch;
    this.octave = other.octave;
    this.volume = other.volume;
  }

  @Override
  public Pitch getPitch() {
    return pitch;
  }

  @Override
  public int getOctave() {
    return octave;
  }

  @Override
  public int getStart() {
    return start;
  }

  @Override
  public int getEnd() {
    return end;
  }

  @Override
  public int getInstrument() {
    return instrument;
  }

  @Override
  public int getVolume() {
    return volume;
  }

  @Override
  public void setPitch(Pitch p) {
    this.pitch = p;
  }

  @Override
  public void setOctave(int o) {
    this.octave = o;
  }

  @Override
  public void setStart(int s) {
    this.start = s;
  }

  @Override
  public void setEnd(int e) {
    this.end = e;
  }

  @Override
  public void setInstrument(int i) {
    this.instrument = i;
  }

  @Override
  public void setVolume(int v) {
    this.volume = v;
  }

  /**
   * @param other other Note to compare to (compares pitch and octave)
   * @return int representing which note is greater
   * @throws IllegalArgumentException if note passed is not this type of Note
   */
  @Override
  public int compareTo(MusicNote other) {
    if (!(other instanceof Note)) {
      throw new IllegalArgumentException("Trying to compare Note with a " +
              "different type of MusicNote");
    }
    Note that = (Note) other;
    if (this.octave == that.octave) {
      int thisPitch = this.pitch.ordinal();
      int otherPitch = that.pitch.ordinal();
      if (thisPitch == otherPitch) {
        return 0;
      } else if (thisPitch < otherPitch) {
        return -1;
      } else {
        return 1;
      }
    } else if (this.octave < that.octave) {
      return -1;
    } else {
      return 1;
    }
  }

  /**
   * Checks if this Note equals the passed in object
   *
   * @param other Object to compare to
   * @return true or false
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Note)) {
      return false;
    }
    Note o = (Note) other;
    return this.pitch == o.pitch &&
            this.start == o.start &&
            this.end == o.end &&
            this.instrument == o.instrument &&
            this.volume == o.volume;
  }

  /**
   * Turns this Note into a string, by using it's pitch and octave combo
   *
   * @return String representing this note, such as "C4" or "D#7"
   */
  @Override
  public String toString() {
    return pitch.toString() + octave;
  }

  @Override
  public int getPitchOctaveNumber() {
    return (pitch.ordinal() + (octave * 12) + 12);
  }
}
