package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.music.controller.MusicController;
import cs3500.music.model.MusicInterface;
import cs3500.music.util.CompositionBuilderImpl;
import cs3500.music.util.MusicReader;
import cs3500.music.view.CompositeViewImpl;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewFrame;
import cs3500.music.view.MusicViewInterface;
import cs3500.music.view.ViewCreator;

public final class MusicEditor {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("No arguments were given. Please enter file-name and view-type as " +
              "command line arguments");
    }
    Readable file = null;
    try {
      file = new FileReader(args[0]);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
    MusicInterface model = MusicReader.parseFile(file, new CompositionBuilderImpl());
    MusicViewInterface view = ViewCreator.create(args[1]);
    if (view instanceof GuiView) {
      GuiView visual = (GuiView) view;
      MusicController controller = new MusicController(model, visual);
    } else {
      view.showView(model);
      try {
        Thread.sleep(10000000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
