package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Class encoding reference to an invalid user id.
 */
public class NoSuchUserException extends CommandException {

  @java.io.Serial
  private static final long serialVersionUID = 202501091828L;

  /**
   * @param id unknown user id
   */
  public NoSuchUserException(int id) {
    super(Message.noSuchUser(id));
  }
}
