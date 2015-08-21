package com.nighthacking.ui;

import com.nighthacking.recipe.Display;
import com.nighthacking.recipe.RecipeRunner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class UIRecipeRunner extends RecipeRunner {

  private Display display;
  
  @Override
  public Display getDisplay() {
    if (display == null) {
      new Thread(() -> Application.launch(UIDisplay.class)).start();
      System.out.println("sleeping");
      try {
        Thread.sleep(12000);
      } catch (InterruptedException ex) {
        Logger.getLogger(UIRecipeRunner.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println("done sleeping");
      display = UIDisplay.instance;
    }
    return display;
  }
}
