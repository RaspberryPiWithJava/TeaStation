package com.nighthacking.recipe;

import com.nighthacking.scales.Scale;
import com.nighthacking.temperature.TemperatureSensor;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public interface RecipeEnvironment {

  Display getDisplay();

  Scale getScale();
  
  void setScale(Scale scale);

  TemperatureSensor getTemperatureSensor();

  void setTemperatureSensor(TemperatureSensor tempSensor);
}
