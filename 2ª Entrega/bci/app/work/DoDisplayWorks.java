package bci.app.work;

import java.util.List;
import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.core.Work;

/**
 * Command to display all works.
 */
class DoDisplayWorks extends Command<LibraryManager> {

  DoDisplayWorks(LibraryManager receiver) {
    super(Label.SHOW_WORKS, receiver);
  }

  @Override
  protected final void execute() {
    List<Work> works = _receiver.getWorks();
    
    for (Work work : works){
      _display.addLine(work.toString());
    }
    _display.display();
  }
}
