package com.nighthacking.recipe;

import com.nighthacking.scales.Scale;
import com.nighthacking.temperature.TemperatureSensor;
import java.util.concurrent.TimeUnit;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class Step {

  private final StepCommand command;
  private final boolean requiresAction;

  private Step(StepCommand command) {
    this(command, false);
  }

  private Step(StepCommand command, boolean requiresInput) {
    this.command = command;
    this.requiresAction = requiresInput;
  }

  public boolean requiresAction() {
    return requiresAction;
  }

  public void execute(RecipeEnvironment env) throws InterruptedException {
    command.execute(env);
  }

  public static Step say(String string, Object... args) {
    return new Step(e -> e.getDisplay().say(string, args));
  }

  public static Step waitForFull() {
    return new Step(e -> e.getScale().waitForStable(w -> w > 0), true);
  }

  public static Step waitForClear() {
    return new Step(e -> e.getScale().waitFor(w -> w <= 0), true);
  }

  public static Step waitForContents() {
    return new Step(e -> {
      Scale s = e.getScale();
      final double originalWeight = s.getWeight();
      s.waitForStable(w -> w != originalWeight);
    }, true);
  }

  public static Step tare() {
    return new Step(e -> {
      try {
        e.getScale().tare();
      } catch (UnsupportedOperationException ex) {
        e.getDisplay().say("Press the 'tare' button on the scale.");
        e.getScale().waitFor(w -> w == 0);
      }
    });
  }

  public static Step waitFor(Ingredient ingredient) {
    return waitFor(ingredient, ingredient.getWeight() / 10);
  }

  public static Step waitFor(Ingredient ingredient, double margin) {
    return new Step(e -> {
      Scale scale = e.getScale();
      double initialWeight = scale.getWeight();
      double target = ingredient.getWeight();
      scale.waitForStable(w -> {
        e.getDisplay().progress((w - initialWeight) / (target - initialWeight),
            "Current: %,.1fg -  Target: %,.1fg +/-%,.1fg", w, target, margin);
        return Math.abs(w - target) < margin;
      });
    }, true);
  }

  public static Step waitForAtLeast(Ingredient ingredient) {
    return waitForAtLeast(ingredient, ingredient.getWeight() / 10);
  }

  public static Step waitForAtLeast(Ingredient ingredient, double margin) {
    return new Step(e -> {
      Scale scale = e.getScale();
      double initialWeight = scale.getWeight();
      double target = ingredient.getWeight();
      scale.waitFor(w -> {
        e.getDisplay().progress((w - initialWeight) / (target - initialWeight),
            "Current: %,.1fg -  Target: > %,.1fg +/-%,.1fg", w, target, margin);
        return w > target - margin / 2;
      });
    }, true);
  }

  public static Step waitForTemp(double target) {
    return waitForTemp(target, 5);
  }

  public static Step waitForTemp(double target, double margin) {
    return new Step(e -> {
      TemperatureSensor sensor = e.getTemperatureSensor();
      double initialTemp = sensor.getTemperature();
      sensor.waitFor(t -> {
        e.getDisplay().progress((t - initialTemp) / (target - initialTemp),
            "Current: %,.1fC -  Target: %,.1fC - %,.1fC", t, target - margin, target + margin);
        return Math.abs(t - target) < margin;
      });
    }, true);
  }

  public static Step countdown(int seconds) {
    return new Step(e -> {
      for (int i = seconds; i > 1; i--) {
        final int time = i;
        e.getDisplay().progress(((double) seconds - time) / seconds, i + " seconds left");
        TimeUnit.SECONDS.sleep(1);
      }
      e.getDisplay().progress(((double) seconds - 1) / seconds, "1 second left");
      TimeUnit.SECONDS.sleep(1);
    }, true);
  }

  @FunctionalInterface
  private static interface StepCommand {

    public void execute(RecipeEnvironment environment) throws InterruptedException;
  }
}
