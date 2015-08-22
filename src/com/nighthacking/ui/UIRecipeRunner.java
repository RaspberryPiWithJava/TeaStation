package com.nighthacking.ui;

import com.nighthacking.recipe.Display;
import com.nighthacking.recipe.Recipe;
import com.nighthacking.recipe.RecipeRunner;
import com.nighthacking.recipe.Step;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class UIRecipeRunner extends RecipeRunner {

  private final UIController controller;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private Recipe recipe;
  private int currentStep = 0;
  private StepTask currentTask;

  public UIRecipeRunner(UIController controller) {
    this.controller = controller;
  }

  @Override
  public void runRecipe(Recipe recipe) throws InterruptedException {
    this.recipe = recipe;
    initRecipe(recipe);
    executeStep();
  }

  private void executeStep() {
    currentTask = new StepTask(recipe.steps()[currentStep]);
    currentTask.setOnSucceeded(e -> {
      if (!nextStep())
        controller.showRestart();
    });
    executor.submit(currentTask);
  }
  
  public boolean previousStep() {
    controller.hideProgressBar();
    if (currentStep <= 1) return false;
    if (currentTask.isRunning()) currentTask.cancel();
    boolean foundAction = false;
    while (currentStep > 0) {
      currentStep--;
      if (recipe.steps()[currentStep].requiresAction()) {
        if (foundAction) {
          currentStep++;
          break;
        } else {
          foundAction = true;
        }
      }
    }
    executeStep();
    return true;
  }
  
  public boolean nextStep() {
    controller.hideProgressBar();
    if (currentStep >= recipe.steps().length - 1) return false;
    if (currentTask.isRunning()) currentTask.cancel();
    currentStep++;
    executeStep();
    return true;
  }

  @Override
  public Display getDisplay() {
    return controller;
  }

  @Override
  public void close() {
    executor.shutdownNow();
    super.close();
  }

  void restart() {
    currentStep = 0;
    executeStep();
  }
  
  public class StepTask extends Task {

    private final Step step;

    public StepTask(Step step) {
      this.step = step;
    }

    @Override
    protected Object call() throws Exception {
      step.execute(UIRecipeRunner.this);
      return null;
    }
  }

}
