package com.nighthacking.temperature;

import java.util.function.DoublePredicate;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public interface TemperatureSensor {

  /**
   * Connect to the temperature sensor
   *
   * @throws IllegalStateException Thrown if the connection can't be opened.
   */
  void connect() throws IllegalStateException;

  double getTemperature();

  void waitFor(DoublePredicate condition) throws InterruptedException;

  /**
   * Close resources that are in use by the temperature sensor implementation.
   */
  void close();  
}
