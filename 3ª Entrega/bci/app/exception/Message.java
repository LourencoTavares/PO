package bci.app.exception;

/** Messages for menu interactions. */
interface Message {
  /**
   * gera a mensagem que indica que houve problemas a abrir um ficheiro
   *
   * @cause a causa do problema a abrir o ficheiro
   *
   * @return <code>String</code> com a mensagem
   */
  static String problemOpeningFile(Exception cause) {
    return "Problema ao abrir ficheiro: " + cause.getMessage();
  }

  /**
   * <code>noSuchCreator</code> gera a mensagem que indica que um crriador não existe.
   * 
   * @param id é o identificador do criador (<code>string</code>)
   * @return <code>String</code> com a mensagem
   */
  static String noSuchCreator(String id) {
    return "O criador '" + id + "' não existe.";
  }
  
  /**
   * <code>noSuchWork</code> gera a mensagem que indica que uma obra não existe.
   * 
   * @param idWork é o identificador da obra (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String noSuchWork(int idWork) {
    return "A obra " + idWork + " não existe.";
  }

  /**
   * <code>noSuchUser</code> gera a mensagem que indica que um um utente não
   * existe.
   * 
   * @param idUser é o identificador do utente (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String noSuchUser(int idUser) {
    return "O utente " + idUser + " não existe.";
  }

  /**
   * <code>userNotSuspended</code> gera a mensagem que indica que um utente não se
   * encontra suspenso.
   * 
   * @param id é o identificador do utente (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String userNotSuspended(int id) {
    return "O utente " + id + " não se encontra suspenso.";
  }

  /**
   * <code>borrowingRuleFailed</code> gera a mensagem que indica que um utente não pode
   * requisitar uma dada obra por violação de uma das regras de validação.
   * 
   * @param idUser é o identificador do utente (<code>int</code>)
   * @param idWork é o identificador da obra (<code>int</code>)
   * @param idRule é o identificador da regra de validação violada (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String borrowingRuleFailed(int idUser, int idWork, int idRule) {
    return "O utente " + idUser + " não pode requisitar a obra " + idWork + ". Violação da regra " + idRule + ".";
  }

  /**
   * <code>workNotBorrowedByUser</code> gera a mensagem que indica que uma obra
   * não foi requisitada por um utente.
   * 
   * @param idWork é o identificador da obra (<code>int</code>)
   * @param idUser é o identificador do utente (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String workNotBorrowedByUser(int idWork, int idUser) {
    return "A obra " + idWork + " não foi requisitada pelo utente " + idUser + ".";
  }

  /**
   * @param name
   * @param email
   * @return message
   */
  static String userRegistrationFailed(String name, String email) {
    return "User registration failed: name '" + name + "', email '" + email + "'.";
  }
}
