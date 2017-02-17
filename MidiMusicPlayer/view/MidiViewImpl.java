package cs3500.music.view;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import cs3500.music.model.MusicInterface;
import cs3500.music.model.MusicNote;

/**
 * Represents a MIDI view, which actually plays the music
 */
public class MidiViewImpl implements MusicViewInterface {
  protected final Sequencer sequencer;

  /**
   * Constructs a MIDI View, and gets the sequencer from the MIDI system needed to play the song
   */
  public MidiViewImpl() {
    Sequencer seqrTemp = null;
    try {
      seqrTemp = MidiSystem.getSequencer();
      seqrTemp.open();

    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    sequencer = seqrTemp;
  }

  public MidiViewImpl(Sequencer seqr) {
    this.sequencer = seqr;
  }

  public void showView(MusicInterface model) {
    sequencer.setTempoInMPQ(model.getTempo());
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
      sequencer.start();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }

  }

  /**
   * Helper method to create a MIDI event given parameters
   *
   * @param messageType Message type needed for ShortMessage
   * @param instrument  int that represents the MIDI channel/instrument for the message
   * @param pitch       int that represents the MIDI pitch for the note in ShortMessage
   * @param volume      int that represents the volume/velocity for the ShortMessage
   * @param beat        int that represents the timestamp for the midi event (beat to play/stop
   *                    note)
   * @return MidiEvent created from these parameters
   */
  protected MidiEvent createMidiEvent(int messageType, int instrument, int pitch, int volume,
                                    long beat) {
    ShortMessage message = new ShortMessage();
    try {
      message.setMessage(messageType,
              instrument,
              pitch,
              volume);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
      System.exit(1);
    }
    MidiEvent event = new MidiEvent(message, beat);
    return event;
  }
}
