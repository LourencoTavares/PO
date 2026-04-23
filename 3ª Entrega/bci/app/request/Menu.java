package bci.app.request;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;

/** Requests menu. */
public class Menu extends pt.tecnico.uilib.menus.Menu {
  /**
   * @param receiver
   */
  public Menu(LibraryManager receiver) {
    super(Label.TITLE,
      new DoRequestWork(receiver), //
      new DoReturnWork(receiver) //
    );
  }
}
