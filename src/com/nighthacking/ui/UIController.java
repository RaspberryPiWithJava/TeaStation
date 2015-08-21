package com.nighthacking.ui;

import com.sun.javafx.sg.prism.NGCanvas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class UIController implements Initializable {
  
  static UIController instance;
  
  public Button previous;
  public Button next;
  public Button quit;
  public TextArea message;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    instance = this;
  }
  
  public void quit() {
    System.exit(0);
  }

  void setText(String message) {
    System.out.println("setting text to: " + message);
    Platform.runLater(() -> this.message.setText(message));
  }
  
}
