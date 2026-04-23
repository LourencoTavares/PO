package bci.app.work;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Perform search according to miscellaneous criteria.
 */
class DoPerformSearch extends Command<LibraryManager> {

  DoPerformSearch(LibraryManager receiver) {
    super(Label.PERFORM_SEARCH, receiver);
    //FIXME add command fields
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  protected final void execute() {
    //FIXME implement command
  }
}
