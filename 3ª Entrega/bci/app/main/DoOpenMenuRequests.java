package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenMenuRequests extends Command<LibraryManager> {

  DoOpenMenuRequests(LibraryManager receiver) {
    super(Label.OPEN_MENU_REQUESTS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    (new bci.app.request.Menu(_receiver)).open();
  }
}
