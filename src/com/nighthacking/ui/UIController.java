package com.nighthacking.ui;

import com.nighthacking.recipe.Display;
import com.nighthacking.recipes.Tea;
import com.nighthacking.scales.SerialScale;
import com.nighthacking.temperature.OneWireTempSensor;
import java.awt.Toolkit;
import java.util.Formatter;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class UIController implements Display {

  public TextArea message;
  public Label name;
  public Label description;
  public Label status;
  public Button restart;
  public ProgressBar progress;
  private UIRecipeRunner runner;

  public void initialize() throws InterruptedException {
    runner = new UIRecipeRunner(this);
    runner.setScale(SerialScale.createValor7000());
    runner.setTemperatureSensor(new OneWireTempSensor());
    final Tea recipe = new Tea(Tea.TeaType.OOLONG);
    runner.runRecipe(recipe);
    name.setText(recipe.name());
    description.setText(recipe.description());
    progress.setVisible(false);
    status.setVisible(false);
  }

  public void quit() {
    runner.close();
    Platform.exit();
  }

  public void next() {
    if (!runner.nextStep()) {
      Toolkit.getDefaultToolkit().beep();
    }
  }

  public void previous() {
    if (!runner.previousStep()) {
      Toolkit.getDefaultToolkit().beep();
    }
  }

  private void setText(String message) {
    Platform.runLater(() -> this.message.setText(message));
  }

  @Override
  public void say(String message, Object... args) {
    setText(new Formatter().format(message, args).toString());
  }

  @Override
  public void progress(double percent, String statusMessage, Object... args) {
    Platform.runLater(() -> {
      if (!progress.isVisible()) {
        progress.setVisible(true);
        status.setVisible(true);
        status.setText("");
      }
      progress.setProgress(percent);
      status.setText(new Formatter().format(statusMessage, args).toString());
    });
  }

  public void hideProgressBar() {
    if (progress.isVisible()) {
      Platform.runLater(() -> {
        progress.setVisible(false);
        status.setVisible(false);
      });
    }
  }

  void showRestart() {
    Platform.runLater(() -> restart.setVisible(true));
  }
  
  public void restart() {
    runner.restart();
    restart.setVisible(false);
  }
}
