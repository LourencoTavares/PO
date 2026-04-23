package bci.app;

import pt.tecnico.uilib.Dialog;

import bci.core.LibraryManager;
import bci.core.exception.ImportFileException;

/**
 * Main driver for the library management application.
 */
public class App {
  /**
   * @param args
   */
  public static void main(String[] args) {
    try (var ui = Dialog.UI) {

      var mgr = new LibraryManager();
      String datafile = System.getProperty("import");
      if (datafile != null) {
        try {
          mgr.importFile(datafile);
        } catch (ImportFileException e) {
          // file input should always be correct: just present the problem
          // no behavior described: just present the problem
          System.err.println("Error in parsing: " + e.getMessage());
          e.printStackTrace();
        }
      }

      new bci.app.main.Menu(mgr).open();
    }
  }
}
