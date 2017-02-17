package cs3500.music.view;

/**
 * Represents a Factory class, that will construct a MusicViewInterface object from an inputted
 * String
 */
public class ViewCreator {
  /**
   * Creates the appropriate View. Either a console, GUI (visual), play (midi), or composite
   * (visual and midi) view.
   */
  public static MusicViewInterface create(String type) {
    switch (type) {
      case "console":
        return new ConsoleViewImpl(System.out);
      case "visual":
        return new GuiViewFrame();
      case "midi":
        return new MidiViewImpl();
      case "composite":
        return new CompositeViewImpl(new GuiViewFrame(), new StartStopMidiView());
      default:
        throw new IllegalArgumentException("Input must be one of 'console', 'visual' or 'midi'");
    }
  }
}
