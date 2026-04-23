package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import static bci.app.main.Message.currentDate;

class DoDisplayDate extends Command<LibraryManager> {

  DoDisplayDate(LibraryManager receiver) {
    super(Label.DISPLAY_DATE, receiver);
  }

  @Override
  protected final void execute() {
    _display.addLine(currentDate(_receiver.getCurrentDate()));
    _display.display();
  }

}
