package com.nighthacking;

import com.nighthacking.recipe.CommandLineRecipeRunner;
import com.nighthacking.recipe.RecipeRunner;
import com.nighthacking.recipes.CoffeeCalculator;
import com.nighthacking.recipes.PourOverCoffeeJavaOne;
import com.nighthacking.recipes.Tea;
import com.nighthacking.scales.SerialScale;
import com.nighthacking.temperature.OneWireTempSensor;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class JavaScale {

  public static void main(String[] args) throws InterruptedException {
    RecipeRunner runner = new CommandLineRecipeRunner();
    runner.setScale(SerialScale.createValor7000());
    runner.setTemperatureSensor(new OneWireTempSensor());
    runner.runRecipe(new Tea(Tea.TeaType.OOLONG));
    runner.close();
  }
}
