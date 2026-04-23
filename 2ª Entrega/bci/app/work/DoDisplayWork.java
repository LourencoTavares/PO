package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
import bci.core.Work;
import static bci.app.work.Prompt.workId;;

/**
 * Command to display a work.
 */
class DoDisplayWork extends Command<LibraryManager> {

  DoDisplayWork(LibraryManager receiver) {
    super(Label.SHOW_WORK, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    Form f = new Form();
    f.addIntegerField("id", workId());
    f.parse();
    int id = f.integerField("id");

    try {
      Work work = _receiver.getWork(id);
      _display.addLine(work.toString());
      _display.display();
    } catch (NoSuchWorkException e) {
      throw new NoSuchWorkException(id);
    }
  }
}
