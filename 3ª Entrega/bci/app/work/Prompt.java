package bci.app.work;

/** Messages for menu interactions. */
public interface Prompt {

  /**
   * @return message with request for work id.
   */
  static String workId() {
    return "Introduza o número da obra: ";
  }

  static String amountToDecrement() {
    return "Introduza a quantidade a actualizar: ";
  }
 
  /**
   * <code>searchTerm</code> gera a mensagem que indica que é necessário
   * fornecer um termo de pesquisa ao sistema.
   * 
   * @return <code>String</code> com a mensagem
   */
  static String searchTerm() {
    return "Introduza o termo de pesquisa: ";
  }

  static String creatorId() {
    return "Introduza o identificador do criador: ";
  }
}
