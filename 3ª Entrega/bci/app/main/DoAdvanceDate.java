package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.forms.Form;
import bci.core.exception.UnrecognizedEntryException;
import static bci.app.main.Prompt.daysToAdvance;

class DoAdvanceDate extends Command<LibraryManager> {

  DoAdvanceDate(LibraryManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
  }

  @Override
  protected final void execute() {
    Form f = new Form();
    f.addIntegerField("ndays", daysToAdvance());
    f.parse();
    int nDays = f.integerField("ndays");

    if (nDays > 0) {
      try {
        _receiver.advanceDays(nDays);
      } catch (UnrecognizedEntryException e) {
      }
    }
  }
}
