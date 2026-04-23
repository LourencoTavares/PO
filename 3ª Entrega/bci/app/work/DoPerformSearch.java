package bci.app.work;

import bci.core.LibraryManager;
import bci.core.Work;
import pt.tecnico.uilib.menus.Command;
import java.util.List;
import static bci.app.work.Prompt.searchTerm;

class DoPerformSearch extends Command<LibraryManager> {

  DoPerformSearch(LibraryManager receiver) {
    super(Label.PERFORM_SEARCH, receiver);
    addStringField("term", searchTerm());
  }

  @Override
  protected final void execute() {
    String term = stringField("term");
    List<Work> found = _receiver.searchWorks(term);

    if (!found.isEmpty()) {
      for (Work w : found) _display.addLine(w.toString());
      _display.display();
    }
  }
}
