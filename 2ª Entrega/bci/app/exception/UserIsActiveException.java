package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Class encoding reference to an active user.
 */
public class UserIsActiveException extends CommandException {
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

 /**
   * @param id the id of concerned user
   */
  public UserIsActiveException(int id) {
    super(Message.userNotSuspended(id));
  }
}
