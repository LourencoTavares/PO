package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Class encoding reference to an invalid work id.
 */
public class NoSuchWorkException extends CommandException {
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

  /**
   * @param id unknown work id
   */
  public NoSuchWorkException(int id) {
    super(Message.noSuchWork(id));
  }
}
