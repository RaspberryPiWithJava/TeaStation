package com.nighthacking.recipe;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public interface Display {

  void say(String message, Object... args);

  void progress(double percent, String message, Object... args);
}
