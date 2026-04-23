package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.UserIsActiveException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * 4.2.5. Settle a fine.
 */
class DoPayFine extends Command<LibraryManager> {

  DoPayFine(LibraryManager receiver) {
    super(Label.PAY_FINE, receiver);
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException {
    //FIXME implement command
  }
}
