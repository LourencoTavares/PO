package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Class representing the case where there was an error while
 * processing a file with a serialized application.
 */
public class FileOpenFailedException extends CommandException {
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

  /**
   * @param cause the exception that prevented the successful desserialization process
   */
  public FileOpenFailedException(Exception cause) {
    super(Message.problemOpeningFile(cause), cause);
  }
}
