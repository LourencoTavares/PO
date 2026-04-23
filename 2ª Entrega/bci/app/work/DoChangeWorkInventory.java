package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Change the number of exemplars of a work.
 */
class DoChangeWorkInventory extends Command<LibraryManager> {
  DoChangeWorkInventory(LibraryManager receiver) {
    super(Label.CHANGE_WORK_INVENTORY, receiver);
     //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException {
    //FIXME implement command
  }
}
