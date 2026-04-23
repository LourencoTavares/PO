package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import static bci.app.work.Prompt.workId;
import static bci.app.work.Prompt.amountToDecrement;




class DoChangeWorkInventory extends Command<LibraryManager> {
  DoChangeWorkInventory(LibraryManager receiver) {
    super(Label.CHANGE_WORK_INVENTORY, receiver);
    addIntegerField("id", workId());
    addIntegerField("amount", amountToDecrement());
  }

  @Override
  protected final void execute() throws NoSuchWorkException {
    int id = integerField("id");
    int amount = integerField("amount");

    boolean ok = _receiver.changeWorkAvailable(id, amount);
    if (!ok) {
      _display.popup(Message.notEnoughInventory(id, amount));
    }
  }
}
