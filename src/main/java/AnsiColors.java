package src.main.java;

public class AnsiColors {

  private static boolean enableColors = true;

  public static void setEnableColors(boolean enable) {
      enableColors = enable;
  }

  public static String reset() {
    return enableColors ? "\u001B[0m" : "";
  }

  public static String bold() {
    return enableColors ? "\u001B[1m" : "";
  }

  public static String red() {
    return enableColors ? "\u001B[31m" : "";
  }

  public static String green() {
    return enableColors ? "\u001B[32m" : "";
  }

  public static String yellow() {
    return enableColors ? "\u001B[33m" : "";
  }

  public static String blue() {
    return enableColors ? "\u001B[34m" : "";
  }
}
