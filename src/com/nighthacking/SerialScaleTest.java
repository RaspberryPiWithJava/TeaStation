package com.nighthacking;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class SerialScaleTest {
  
  static String data = "";

  public static void main(String[] args) throws InterruptedException {
    Serial serial = SerialFactory.createInstance();
    serial.open(Serial.DEFAULT_COM_PORT, 2400);
    serial.addListener((sde) -> {
//      System.out.println("sde.getData() = " + sde.getData());
      data = data.concat(sde.getData());
      String[] measurements = data.split("\r\n", -1);
      for (int i=0; i<measurements.length - 1; i++) {
        System.out.println("measurements[i] = " + measurements[i]);
      }
      data = measurements[measurements.length - 1];
    });
    serial.writeln("CA");
    TimeUnit.SECONDS.sleep(30);
    serial.shutdown();
  }
}
