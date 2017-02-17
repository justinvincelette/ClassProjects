package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevindo on 6/21/16. Controller to handle input from keyboard
 */
public class KeyboardHandler implements KeyListener {
  private final Map<Integer, Runnable> typed, pressed, released;

  /**
   * Constructor for the {@link KeyboardHandler}
   *
   * @param typed    the {@code Map} for keys typed
   * @param pressed  the {@code Map} for keys pressed
   * @param released the {@code Map} for keys released
   */
  private KeyboardHandler(Map<Integer, Runnable> typed, Map<Integer, Runnable> pressed,
                          Map<Integer, Runnable> released) {
    this.typed = typed;
    this.pressed = pressed;
    this.released = released;
  }

  /**
   * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a
   * definition of a key typed event.
   */
  @Override
  public void keyTyped(KeyEvent e) {
    if (this.typed.get(e.getKeyCode()) != null) {
      this.typed.get(e.getKeyCode()).run();
    }
  }

  /**
   * Invoked when a key has been pressed. See the class description for {@link KeyEvent} for a
   * definition of a key pressed event.
   */
  @Override
  public void keyPressed(KeyEvent e) {
    if (this.pressed.get(e.getKeyCode()) != null) {
      this.pressed.get(e.getKeyCode()).run();
    }
  }

  /**
   * Invoked when a key has been released. See the class description for {@link KeyEvent} for a
   * definition of a key released event.
   */
  @Override
  public void keyReleased(KeyEvent e) {
    if (this.released.get(e.getKeyCode()) != null) {
      this.released.get(e.getKeyCode()).run();
    }
  }

  /**
   * Builder for a {@link KeyboardHandler}
   */
  public static final class Builder {
    private final Map<Integer, Runnable> typed = new HashMap<>();
    private final Map<Integer, Runnable> pressed = new HashMap<>();
    private final Map<Integer, Runnable> released = new HashMap<>();

    /**
     * Constructs a {@link KeyboardHandler} from given {@code Map}s for each type of key handler
     *
     * @return the keyboard handler
     */
    public KeyboardHandler build() {
      return new KeyboardHandler(this.typed, this.pressed, this.released);
    }

    /**
     * Adds a typed key {@link Runnable} for a specific {@code keyCode}
     *
     * @param keyCode  the key code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addKeyTyped(int keyCode, Runnable runnable) {
      this.typed.put(keyCode, runnable);
      return this;
    }

    /**
     * Adds a pressed key {@link Runnable} for a specific {@code keyCode}
     *
     * @param keyCode  the key code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addKeyPressed(int keyCode, Runnable runnable) {
      this.pressed.put(keyCode, runnable);
      return this;
    }

    /**
     * Adds a released key {@link Runnable} for a specific {@code keyCode}
     *
     * @param keyCode  the key code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addKeyReleased(int keyCode, Runnable runnable) {
      this.released.put(keyCode, runnable);
      return this;
    }
  }
}
