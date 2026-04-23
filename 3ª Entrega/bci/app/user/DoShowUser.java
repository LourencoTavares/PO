package bci.app.user;

import bci.core.LibraryManager;
import bci.core.User;
import bci.app.exception.NoSuchUserException;
import pt.tecnico.uilib.menus.Command;
import static bci.app.user.Prompt.userId;

class DoShowUser extends Command<LibraryManager> {

  DoShowUser(LibraryManager receiver) {
    super(Label.SHOW_USER, receiver);
    addIntegerField("id", userId());
  }

  @Override
  protected final void execute() throws NoSuchUserException {
    int id = integerField("id");
    User u = _receiver.getUser(id);

    String line = u.getId() + " - " + u.getName() + " - " + u.getEmail() + " - " + u.getBehavior() + " - " + (u.isActive() ? "ACTIVO" : "SUSPENSO");

    if (!u.isActive()) {
      line += " - EUR " + u.getFine();
    }

    _display.addLine(line);
    _display.display();
  }
}