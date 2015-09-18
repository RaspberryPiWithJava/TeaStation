package com.nighthacking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class TeaStation extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("ui/TeaStationUI.fxml"));
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
    stage.setScene(scene);
    scene.getRoot().setScaleX(bounds.getWidth() / 1280);
    scene.getRoot().setScaleY(bounds.getHeight() / 800);

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
