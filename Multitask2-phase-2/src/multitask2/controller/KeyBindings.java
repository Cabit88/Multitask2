package multitask2.controller;

import java.util.Objects;

import com.github.forax.zen.KeyboardEvent.Key;

/** Définit toutes les touches du jeu à un seul endroit. */
public final class KeyBindings {
  private KeyBindings() {}

  public enum Layout { QWERTY, AZERTY }

  private static Layout layout = Layout.AZERTY;

  public static void setLayout(Layout value) {
    layout = Objects.requireNonNull(value);
  }

  public static Layout layout() {
    return layout;
  }

  public static Key left() { return Key.LEFT; }
  public static Key right() { return Key.RIGHT; }
  public static Key up() { return Key.UP; }
  public static Key down() { return Key.DOWN; }
  public static Key jump() { return Key.SHIFT; }
  public static Key fly() { return Key.SPACE; }
  public static Key touchUp() { return layout == Layout.AZERTY ? Key.Z : Key.W; }
  public static Key touchLeft() { return layout == Layout.AZERTY ? Key.Q : Key.A; }
  public static Key touchDown() { return Key.S; }
  public static Key touchRight() { return Key.D; }
  public static Key circleLeft() { return layout == Layout.AZERTY ? Key.A : Key.Q; }
  public static Key circleRight() { return Key.E; }

  public static Key testTask1() { return Key.W; }
  public static Key testTask2() { return Key.X; }
  public static Key testTask3() { return Key.C; }
  public static Key testTask4() { return Key.V; }
  public static Key testTask5() { return Key.B; }
  public static Key testTask6() { return Key.N; }
}
