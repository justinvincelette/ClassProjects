package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevindo on 6/21/16. Controller to handle input from the mouse
 */
public class MouseHandler implements MouseListener {
  private final Map<Integer, Runnable> mouseClicked, mousePressed, mouseReleased, mouseEntered,
          mouseExited;
  private MouseEvent currentMouseEvent;

  /**
   * Constructor for the {@link MouseHandler}
   *
   * @param mouseClicked  the {@code Map} for mouse clicks
   * @param mousePressed  the {@code Map} for mouse presses
   * @param mouseReleased the {@code Map} for mouse releases
   * @param mouseEntered  the {@code Map} for mouse enters
   * @param mouseExited   the {@code Map} for mouse exits
   */
  private MouseHandler(Map<Integer, Runnable> mouseClicked, Map<Integer, Runnable> mousePressed,
                       Map<Integer, Runnable> mouseReleased, Map<Integer, Runnable> mouseEntered,
                       Map<Integer, Runnable> mouseExited) {
    this.mouseClicked = mouseClicked;
    this.mousePressed = mousePressed;
    this.mouseReleased = mouseReleased;
    this.mouseEntered = mouseEntered;
    this.mouseExited = mouseExited;
  }

  /**
   * Invoked when the mouse button has been clicked (pressed and released) on a component.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    this.currentMouseEvent = e;
    if (this.mouseClicked.get(e.getButton()) != null) {
      this.mouseClicked.get(e.getButton()).run();
    }
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    this.currentMouseEvent = e;
    if (this.mousePressed.get(e.getButton()) != null) {
      this.mousePressed.get(e.getButton()).run();
    }
  }

  /**
   * Invoked when a mouse button has been released on a component.
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    this.currentMouseEvent = e;
    if (this.mouseReleased.get(e.getButton()) != null) {
      this.mouseReleased.get(e.getButton()).run();
    }
  }

  /**
   * Invoked when the mouse enters a component.
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    this.currentMouseEvent = e;
    if (this.mouseEntered.get(e.getButton()) != null) {
      this.mouseEntered.get(e.getButton()).run();
    }
  }

  /**
   * Invoked when the mouse exits a component.
   */
  @Override
  public void mouseExited(MouseEvent e) {
    this.currentMouseEvent = e;
    if (this.mouseExited.get(e.getButton()) != null) {
      this.mouseExited.get(e.getButton()).run();
    }
  }

  /**
   * Gets the current mouse event
   *
   * @return current mouse event
   */
  public MouseEvent getCurrentMouseEvent() {
    return this.currentMouseEvent;
  }

  /**
   * Builder for a {@link MouseHandler}
   */
  public static final class Builder {
    private final Map<Integer, Runnable> mouseClicked = new HashMap<>();
    private final Map<Integer, Runnable> mousePressed = new HashMap<>();
    private final Map<Integer, Runnable> mouseReleased = new HashMap<>();
    private final Map<Integer, Runnable> mouseEntered = new HashMap<>();
    private final Map<Integer, Runnable> mouseExited = new HashMap<>();

    /**
     * Constructs a {@link MouseHandler} from given {@code Map}s for each type of mouse handler
     *
     * @return the mouse handler
     */
    public MouseHandler build() {
      return new MouseHandler(this.mouseClicked, this.mousePressed, this.mouseReleased,
              this.mouseEntered, this.mouseExited);
    }

    /**
     * Adds a clicked mouse {@link Runnable} for a specific {@code mouse button}
     *
     * @param button   the button code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addMouseClicked(int button, Runnable runnable) {
      this.mouseClicked.put(button, runnable);
      return this;
    }

    /**
     * Adds a pressed mouse {@link Runnable} for a specific {@code mouse button}
     *
     * @param button   the button code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addMousePressed(int button, Runnable runnable) {
      this.mousePressed.put(button, runnable);
      return this;
    }

    /**
     * Adds a released mouse {@link Runnable} for a specific {@code mouse button}
     *
     * @param button   the button code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addMouseReleased(int button, Runnable runnable) {
      this.mouseReleased.put(button, runnable);
      return this;
    }

    /**
     * Adds a entered mouse {@link Runnable} for a specific {@code mouse button}
     *
     * @param button   the button code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addMouseEntered(int button, Runnable runnable) {
      this.mouseEntered.put(button, runnable);
      return this;
    }

    /**
     * Adds a exited mouse {@link Runnable} for a specific {@code mouse button}
     *
     * @param button   the button code
     * @param runnable the {@link Runnable} to add
     * @return This builder
     */
    public Builder addMouseExited(int button, Runnable runnable) {
      this.mouseExited.put(button, runnable);
      return this;
    }
  }
}
