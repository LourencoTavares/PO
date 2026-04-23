package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * <code>UserRegistrationFailedException</code> é uma classe que implementa a excepção a
 * lançar quando se tenta registar um utilizador com parâmetros inválidos.
 */
public class UserRegistrationFailedException extends CommandException {
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

 /**
   * @param name O nome do utilizador que deveria ter sido registado 
   * @param email O email do utilizador que deveria ter sido registado 
   */
  public UserRegistrationFailedException(String name, String email) {
    super(Message.userRegistrationFailed(name, email));
  }
}
