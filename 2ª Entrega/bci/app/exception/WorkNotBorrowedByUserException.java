package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * <code>WorkNotBorrowedByUserException</code> é uma classe que implementa a excepção a
 * lançar quando um utente pretende devolver uma obra que não requisitou.
 */
public class WorkNotBorrowedByUserException extends CommandException {
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

  /** O id do utente que pretende devolver a obra */
  private final int _userId;

  /** O id da obra que se pretende devolver obra */
  private final int _workId;

  /**
   * @param idWork id da obra que se pretende devolver 
   * @param idUser id do utente que pretende devolver a obra
   */
  public WorkNotBorrowedByUserException(int idWork, int idUser) {
    super(Message.workNotBorrowedByUser(idWork, idUser));
    _workId = idWork;
    _userId = idUser;
  }
}
