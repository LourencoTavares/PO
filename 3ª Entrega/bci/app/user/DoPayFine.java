package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.UserIsActiveException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import static bci.app.user.Prompt.userId;


class DoPayFine extends Command<LibraryManager> {

  DoPayFine(LibraryManager receiver) {
    super(Label.PAY_FINE, receiver);
    addIntegerField("id", userId());
  }

  @Override
  protected final void execute() throws CommandException {
    int userId = integerField("id");
    try {
      _receiver.payUserFine(userId);
    } catch (UserIsActiveException | NoSuchUserException e) {
      throw e;
    }
  }
}
