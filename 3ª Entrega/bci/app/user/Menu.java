package bci.app.user;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;

/** User menu. */
public class Menu extends pt.tecnico.uilib.menus.Menu {
  /**
   * @param receiver
   */
  public Menu(LibraryManager receiver) {
    super(Label.TITLE,
      new DoRegisterUser(receiver),
      new DoShowUser(receiver),
      new DoShowUsers(receiver),
      new DoShowUserNotifications(receiver),
      new DoPayFine(receiver)
    );
  }
}
