package com.nighthacking.temperature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.function.DoublePredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class OneWireTempSensor implements TemperatureSensor {

  private Path sensorFile;
  private Thread tempListener;
  private volatile boolean running = true;

  @Override
  public void connect() throws IllegalStateException {
    Path w1Folder = Paths.get("/sys/bus/w1/devices");
    try {
      Optional<Path> deviceFolder = Files.list(w1Folder).filter(
          f -> f.getFileName().toString().startsWith("28")).findFirst();
      if (!deviceFolder.isPresent()) {
        throw new IllegalStateException("No 1-wire device found");
      }
      sensorFile = deviceFolder.get().resolve("w1_slave");
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
    tempListener = new Thread(this::pollForTemperature);
    tempListener.start();
  }

  private double temperature;
  private final Phaser tempPhaser = new Phaser(1);

  public void pollForTemperature() {
    while (running) {
      try (Stream<String> lines = Files.lines(sensorFile)) {
        Optional<String> temp = lines.filter(l -> l.contains("t=")).findAny();
        if (temp.isPresent()) {
          temperature = Integer.parseInt(temp.get().substring(
              temp.get().indexOf("t=") + 2)) / 1000d;
          tempPhaser.arrive();
        }
      } catch (IOException ex) {
        Logger.getLogger(OneWireTempSensor.class.getName()).log(
            Level.SEVERE, "Error reading from sensor", ex);
      }
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (InterruptedException ex) { /* continue on */ }
    }
  }

  @Override
  public double getTemperature() {
    return temperature;
  }

  @Override
  public void waitFor(DoublePredicate condition) throws InterruptedException {
    while (!condition.test(temperature)) {
      tempPhaser.awaitAdvanceInterruptibly(tempPhaser.getPhase());
    }
  }

  @Override
  public void close() {
    running = false;
  }
}
