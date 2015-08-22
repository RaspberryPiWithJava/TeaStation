package com.nighthacking.recipe;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class CommandLineRecipeRunner extends RecipeRunner {

  private final Display display = new CommandLineDisplay();

  @Override
  public Display getDisplay() {
    return display;
  }

  static class CommandLineDisplay implements Display {

    @Override
    public void say(String message, Object... args) {
      System.out.format(message, args);
    }

    @Override
    public void progress(double percent, String message, Object... args) {
      System.out.format(message);
    }
  }
}
