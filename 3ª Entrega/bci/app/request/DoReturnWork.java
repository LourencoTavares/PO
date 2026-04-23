package bci.app.request;

import bci.core.LibraryManager;
import bci.core.User;
import bci.core.Work;
import bci.core.Notification;
import java.util.List;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.WorkNotBorrowedByUserException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import static bci.app.request.Message.showFine;
import static bci.app.request.Prompt.finePaymentChoice;

class DoReturnWork extends Command<LibraryManager> {

  DoReturnWork(LibraryManager receiver) { super(Label.RETURN_WORK, receiver); }

  @Override
  protected final void execute() throws CommandException {
    final int userId = Form.requestInteger("Introduza o número de utente: ");
    final int workId = Form.requestInteger("Introduza o número da obra: ");

    final User u = _receiver.getUser(userId);
    final Work w = _receiver.getWork(workId);

    if (!_receiver.hasBorrowed(u.getId(), w.getId())) {
      throw new WorkNotBorrowedByUserException(w.getId(), u.getId());
    }

    final int prevAvail = w.getAvailableCopies();

    w.changeAvailableCopies(1);
    _receiver.returnWork(u.getId(), w.getId());

    _receiver.closeOneOpenRequest(u.getId(), w.getId());

    if (prevAvail == 0 && w.getAvailableCopies() > 0) {
      List<Integer> waiting = _receiver.getWaitingUsers(w.getId());
      for (int uid : waiting) {
        try {
          User uu = _receiver.getUser(uid);
          uu.addNotification(new Notification("DISPONIBILIDADE: " + w.toString()));
        } catch (NoSuchUserException ignore){
        }
      }
    }

    if (u.getFine() > 0) {
      int fine = u.getFine(); 
      _display.addLine(showFine(userId, fine));
      _display.display();

      pt.tecnico.uilib.forms.Form f = new pt.tecnico.uilib.forms.Form();
      f.addBooleanField("pay", finePaymentChoice());
      f.parse();

      if (f.booleanField("pay")) {
        try {
          _receiver.payUserFine(u.getId());
        } catch (bci.app.exception.UserIsActiveException e) {
        } catch (bci.app.exception.NoSuchUserException e) {
        }
      }
    }
  }
}
