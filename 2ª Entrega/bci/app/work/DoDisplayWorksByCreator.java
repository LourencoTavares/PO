package bci.app.work;

import bci.core.LibraryManager;
import bci.core.Work;
import bci.app.exception.NoSuchCreatorException;
import pt.tecnico.uilib.menus.Command;

import static bci.app.work.Prompt.creatorId;

import java.util.List;

/** Display all works by a specific creator. */
class DoDisplayWorksByCreator extends Command<LibraryManager> {

  DoDisplayWorksByCreator(LibraryManager receiver) {
    super(Label.SHOW_WORKS_BY_CREATOR, receiver);
    addStringField("name",creatorId());
  }

  @Override
  protected final void execute() throws NoSuchCreatorException {
    String name = stringField("name");
    List<Work> works = _receiver.getWorksByCreator(name);

    if (!works.isEmpty()) {
      for (Work w : works) _display.addLine(w.toString());
      _display.display();
    }
  }
}
