package cs3500.music.view;

import cs3500.music.model.MusicInterface;

/**
 * Enforces the methods that a Music View should implement
 */
public interface MusicViewInterface {
  /**
   * Shows/Outputs the appropriate view from the given model
   */
  void showView(MusicInterface model);
}
