package com.nighthacking.recipes;

import com.nighthacking.recipe.Step;
import com.nighthacking.recipe.Ingredient;
import com.nighthacking.recipe.Recipe;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class Tea implements Recipe {
  
  public static enum TeaType {
    WHITE(74, 3, 120),
    GREEN(80, 3, 120),
    OOLONG(88, 6, 90),
    BLACK(91, 3, 120),
    PUER(97, 3, 180);
    private final double temperature;
    private final double margin;
    private final int time;
    private TeaType(double temperature, double margin, int time) {
      this.temperature = temperature;
      this.margin = margin;
      this.time = time;
    }
  }

  private final TeaType type;
  private static final double CUP_SIZE = 200; // grams
  private final Ingredient tea;
  private final Ingredient water;

  public Tea(TeaType type) {
    tea = Ingredient.byWeight(2 * CUP_SIZE/100, "Tea"); // ISO 3103 Tea Standard
    water = Ingredient.byWeight(CUP_SIZE, "Water");
    this.type = type;
  }

  @Override
  public String name() {
    return "Tea Station";
  }
  
  @Override
  public String description() {
    return "Precisely steeps 1 cup of tea using a scale and temperature sensor.";
  }

  @Override
  public Ingredient[] ingredients() {
    return new Ingredient[]{tea, water};
  }

  @Override
  public Step[] steps() {
    return new Step[]{
      Step.say("Boil water to " + type.temperature + "C"),
      Step.waitForTemp(type.temperature, type.margin),
      Step.say("Add " + tea),
      Step.waitFor(tea),
      Step.tare(),
      Step.say("Now pour " + water),
      Step.waitFor(tea),
      Step.countdown(type.time),
      Step.say("Remove your leaves from the tea and enjoy!")
    };
  }
}
