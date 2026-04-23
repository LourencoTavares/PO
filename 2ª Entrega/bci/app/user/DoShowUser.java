package bci.app.user;

import bci.core.LibraryManager;
import bci.core.User;
import bci.app.exception.NoSuchUserException;
import pt.tecnico.uilib.menus.Command;

import static bci.app.user.Prompt.userId;

/**
 * 4.2.2. Show specific user.
 */
class DoShowUser extends Command<LibraryManager> {

  DoShowUser(LibraryManager receiver) {
    super(Label.SHOW_USER, receiver);
    addIntegerField("id", userId());
  }

  @Override
  protected final void execute() throws NoSuchUserException {
    User u = _receiver.getUser(integerField("id"));
    _display.addLine(u.toString());
    _display.display();
  }
}