package bci.app.request;

import bci.core.Category;
import bci.core.LibraryManager;
import bci.core.User;
import bci.core.UserBehavior;
import bci.core.Work;
import bci.app.exception.BorrowingRuleFailedException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRequestWork extends Command<LibraryManager> {

  DoRequestWork(LibraryManager receiver) {
    super(Label.REQUEST_WORK, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    final int userId = Form.requestInteger("Introduza o número de utente: ");
    final int workId = Form.requestInteger("Introduza o número da obra: ");

    final User u = _receiver.getUser(userId);
    final Work w = _receiver.getWork(workId);

    final int loanDays = _receiver.computeLoanDays(u, w);
    final int today = _receiver.getCurrentDate();

    _receiver.autoSuspendIfOverdue(u.getId());
    if (_receiver.hasBorrowed(u.getId(), w.getId()))
      throw new BorrowingRuleFailedException(u.getId(), w.getId(), 1);
    if (!u.isActive())
      throw new BorrowingRuleFailedException(u.getId(), w.getId(), 2);

    if (w.getAvailableCopies() <= 0) {
      Form f = new Form();
      f.addBooleanField("notify", Prompt.returnNotificationPreference());
      f.parse();
      if (f.booleanField("notify")) {
        _receiver.addWaitingUser(w.getId(), u.getId());
      }
      return;
    }

    final int open = _receiver.countOpenBorrowings(u.getId());
    final int cap = switch (u.getBehavior()) {
      case CUMPRIDOR -> 5;
      case FALTOSO -> 1;
      default -> 3;
    };
    if (open >= cap) throw new BorrowingRuleFailedException(u.getId(), w.getId(), 4);

    if (w.getCategory() == Category.REFERENCE)
      throw new BorrowingRuleFailedException(u.getId(), w.getId(), 5);

    if (u.getBehavior() != UserBehavior.CUMPRIDOR && w.getPrice() > 2500)
      throw new BorrowingRuleFailedException(u.getId(), w.getId(), 6);

    final int days = _receiver.computeLoanDays(u, w);
    final int deadline = _receiver.getCurrentDate() + days;

    w.changeAvailableCopies(-1);
    _receiver.borrowWork(u.getId(), w.getId());
    _receiver.createRequest(u, w, today, loanDays);

    _display.addLine(Message.workReturnDay(w.getId(), deadline));
    _display.display();
  }
}
