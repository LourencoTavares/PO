package bci.app.user;

import bci.core.LibraryManager;
import bci.core.User;
import bci.core.Notification;
import bci.app.exception.NoSuchUserException;
import pt.tecnico.uilib.menus.Command;
import static bci.app.user.Prompt.userId;


class DoShowUserNotifications extends Command<LibraryManager> {

  DoShowUserNotifications(LibraryManager receiver) {
    super(Label.SHOW_USER_NOTIFICATIONS, receiver);
    addIntegerField("id", userId());
  }

  @Override
  protected final void execute() throws NoSuchUserException {
    User u = _receiver.getUser(integerField("id"));
    
    for (Notification n : u.getNotifications()) {
      _display.addLine(n.toString());
    }
    _display.display();
    u.clearNotifications();
  }
}