package com.nighthacking.ui;

import com.nighthacking.recipe.Display;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class UIDisplay extends Application implements Display {

  static UIDisplay instance;

  public UIDisplay() {
    instance = this;
  }

  @Override
  public void say(String message) {
    System.out.println("message = " + message);
    UIController.instance.setText(message);
  }

  @Override
  public void say(String message, Object... args) {
    System.out.println("message = " + message);
    UIController.instance.setText(new Formatter().format(message, args).toString());
  }

  @Override
  public void countdown(int seconds) throws InterruptedException {
    for (int i = seconds; i > 1; i--) {
      say(i + " seconds left");
      TimeUnit.SECONDS.sleep(1);
    }
    say("1 second left");
    TimeUnit.SECONDS.sleep(1);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("CoffeeUI.fxml"));
    Scene scene = new Scene(root, 1280, 800);
    stage.setScene(scene);
    stage.show();
  }

}
