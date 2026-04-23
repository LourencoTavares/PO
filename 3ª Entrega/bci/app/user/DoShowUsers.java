package bci.app.user;

import java.util.List;

import bci.core.LibraryManager;
import bci.core.User;
import pt.tecnico.uilib.menus.Command;

class DoShowUsers extends Command<LibraryManager> {

  DoShowUsers(LibraryManager receiver) {
    super(Label.SHOW_USERS, receiver);
  }

  @Override
  protected final void execute() {
    List<User> users = _receiver.getUsers();

    if (!users.isEmpty()) {
      for (User u : _receiver.getUsers()) {
        _display.addLine(u.toString());
      }
      _display.display();
    }  
  }
}
