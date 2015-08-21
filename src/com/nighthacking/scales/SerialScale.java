package com.nighthacking.scales;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import java.util.concurrent.Phaser;
import java.util.function.DoublePredicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class SerialScale implements Scale {

  private static final String CONTINUOUSAUTOPRINT_SCOUT = "CA";
  private static final String CONTINUOUSAUTOPRINT_VALOR = "CP";
  private static final String GRAMMODE_SCOUT = "0M";
  private static final String GRAMMODE_VALOR = "1U";
  private static final String TARE = "T";
  
  private final int baudRate;
  private final String continuousAutoprint;
  private final String gramMode;
  
  
  public static Scale createScoutPro() {
    return new SerialScale(2400, CONTINUOUSAUTOPRINT_SCOUT, GRAMMODE_SCOUT);
  }
  
  public static Scale createValor7000() {
    return new SerialScale(9600, CONTINUOUSAUTOPRINT_VALOR, GRAMMODE_VALOR);
  }

  private SerialScale(int baudRate, String continuousAutoprint, String gramMode) {
    this.baudRate = baudRate;
    this.continuousAutoprint = continuousAutoprint;
    this.gramMode = gramMode;
  }

  private final Phaser scalePhaser = new Phaser(1);
  private Serial serial;
  private String data = "";
  private volatile double weight;
  private volatile boolean stable;
  
  @Override
  public void connect() {
    serial = SerialFactory.createInstance();
    serial.open(Serial.DEFAULT_COM_PORT, baudRate);
    serial.addListener((sde) -> {
      data = data.concat(sde.getData());
      String[] measurements = data.split("\r\n", -1);
      for (int i=0; i<measurements.length - 1; i++) {
        processMeasurement(measurements[i]);
      }
      data = measurements[measurements.length - 1];
    });
    serial.writeln(continuousAutoprint);
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public boolean isStable() {
    return stable;
  }

  @Override
  public void waitFor(DoublePredicate condition) {
    while (!condition.test(weight)) {
      scalePhaser.awaitAdvance(scalePhaser.getPhase());
    }
  }

  @Override
  public void waitForStable(DoublePredicate condition) {
    while (!stable || !condition.test(weight)) {
      scalePhaser.awaitAdvance(scalePhaser.getPhase());
    }
  }

  @Override
  public void tare() throws UnsupportedOperationException {
    serial.writeln(TARE);
  }

  @Override
  public void close() {
    serial.shutdown();
  }

  private void processMeasurement(String measurement) {
    try {
      String[] components = measurement.split("[ ]+");
      if (!components[2].equals("g")) {
        serial.writeln(gramMode);
        return;
      }
      weight = Double.parseDouble(components[1]);
      stable = components.length <= 3 || !components[3].equals("?");
      scalePhaser.arrive();
    } catch (Throwable e) {
      Logger.getLogger(SerialScale.class.getName()).log(Level.FINER, "Can't process measurement " + measurement, e);
    }
  }
}
