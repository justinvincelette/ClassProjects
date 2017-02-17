package cs3500.music.util;

import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;

/**
 * Represents the implementation for CompositionBuilder
 */
public class CompositionBuilderImpl implements CompositionBuilder<MusicModel> {
  private MusicModel model;

  /**
   * Constructs a CompositionBuilderImpl, by creating a new Model for the private variable
   */
  public CompositionBuilderImpl() {
    model = new MusicModel();
  }

  @Override
  public MusicModel build() {
    return model;
  }

  @Override
  public CompositionBuilder<MusicModel> setTempo(int tempo) {
    model.setTempo(tempo);
    return this;
  }

  @Override
  public CompositionBuilder<MusicModel> addNote(int start, int end, int instrument, int pitch,
                                                int volume) {
    Note n = new Note(start, end, instrument, pitch, volume);
    model.addNote(n);
    return this;
  }
}
