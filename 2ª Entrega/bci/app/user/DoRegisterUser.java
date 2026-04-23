package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.UserRegistrationFailedException;
import pt.tecnico.uilib.menus.Command;

import static bci.app.user.Prompt.userName;
import static bci.app.user.Prompt.userEMail;
import static bci.app.user.Message.registrationSuccessful;

/**
 * 4.2.1. Register new user.
 */
class DoRegisterUser extends Command<LibraryManager> {

  DoRegisterUser(LibraryManager receiver) {
    super(Label.REGISTER_USER, receiver);
    addStringField("name", userName());
    addStringField("email", userEMail());
  }

  @Override
  protected final void execute() throws UserRegistrationFailedException {
    int id = _receiver.registerUser(stringField("name"), stringField("email"));
    _display.addLine(registrationSuccessful(id));
    _display.display();
  }
}
