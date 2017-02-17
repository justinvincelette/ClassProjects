package cs3500.music.view;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * Represents a Midi View that can be stopped/started, added to and removed from, and changed tick
 */
public class StartStopMidiView extends MidiViewImpl implements StartStopViewInterface {
  /**
   * Constructs a StartStopMidiView
   */
  public StartStopMidiView() {
    super();
  }

  @Override
  public void start() {
    this.sequencer.start();
  }

  @Override
  public void stop() {
    this.sequencer.stop();
  }

  @Override
  public long getCurrentTick() {
    return this.sequencer.getTickPosition();
  }

  @Override
  public float getTempo() {
    return this.sequencer.getTempoInMPQ();
  }

  @Override
  public void setTempo(float tempo) {
    this.sequencer.setTempoInMPQ(tempo);
  }

  @Override
  public void addNote(MusicNote n) {
    MidiEvent on = this.createMidiEvent(ShortMessage.NOTE_ON, n.getInstrument(),
            n.getPitchOctaveNumber(), n.getVolume(), n.getStart());
    MidiEvent off = this.createMidiEvent(ShortMessage.NOTE_OFF, n.getInstrument(),
            n.getPitchOctaveNumber(), n.getVolume(), n.getEnd());
    this.sequencer.getSequence().getTracks()[0].add(on);
    this.sequencer.getSequence().getTracks()[0].add(off);
  }

  @Override
  public void removeNote(MusicInterface model, long tickPos) {
    Sequence sequence = null;
    try {
      sequence = new Sequence(Sequence.PPQ, 1);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    Track track = sequence.createTrack();
    Integer key = 0;
    List<MusicNote> notesAtBeat;
    int startingBeat, endingBeat, midiPitch, instrument, volume;
    while (key <= model.getLength()) {
      notesAtBeat = model.getNotesInBeat(key);
      if (notesAtBeat != null) {
        for (MusicNote n : notesAtBeat) {
          startingBeat = n.getStart();
          endingBeat = n.getEnd();
          midiPitch = n.getPitchOctaveNumber();
          instrument = n.getInstrument();
          volume = n.getVolume();
          track.add(createMidiEvent(ShortMessage.NOTE_ON, instrument - 1, midiPitch, volume,
                  startingBeat));
          track.add(createMidiEvent(ShortMessage.NOTE_OFF, instrument - 1, midiPitch, volume,
                  endingBeat));
        }
      }
      key++;
    }
    try {
      sequencer.setSequence(sequence);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    sequencer.setTickPosition(tickPos);
  }

  @Override
  public void jumpToStart() {
    this.sequencer.setTickPosition(0);
  }

  @Override
  public void jumpToEnd() {
    this.sequencer.setTickPosition(this.sequencer.getTickLength() - 1);
  }
}
