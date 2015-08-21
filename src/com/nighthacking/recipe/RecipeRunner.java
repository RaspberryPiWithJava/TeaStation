package com.nighthacking.recipe;

import com.nighthacking.scales.Scale;
import com.nighthacking.temperature.TemperatureSensor;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public abstract class RecipeRunner implements RecipeEnvironment {

  private Scale scale;
  private TemperatureSensor tempSensor;

  @Override
  public Scale getScale() {
    return scale;
  }

  @Override
  public void setScale(Scale scale) {
    this.scale = scale;
    scale.connect();
  }

  @Override
  public TemperatureSensor getTemperatureSensor() {
    return tempSensor;
  }

  @Override
  public void setTemperatureSensor(TemperatureSensor tempSensor) {
    this.tempSensor = tempSensor;
    tempSensor.connect();
  }

  public void runRecipe(Recipe recipe) throws InterruptedException {
    getDisplay().say(recipe.description());
    if (getScale() != null) {
      getScale().waitFor(w -> getScale().isStable());
      if (getScale().getWeight() != 0) {
        getDisplay().say("Please empty the scale before we begin");
        getScale().waitFor(w -> w == 0);
      }
    }

    for (Step step : recipe.steps()) {
      step.execute(this);
    }
  }

  public void close() {
    if (scale != null) {
      scale.close();
    }
    if (tempSensor != null) {
      tempSensor.close();
    }
  }
}
