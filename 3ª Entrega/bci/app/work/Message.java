package bci.app.work;

/** Messages for menu interactions. */
interface Message {

  /**
   * <code>inexistente</code> gera a mensagem que indica que uma obra não existe.
   * 
   * @param idWork é o identificador da obra (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String noSuchWork(int idWork) {
    return "A obra " + idWork + " não existe.";
  }

  /**
   * <code>inválida</code> gera a mensagem que indica que um número de obra é
   * inválido.
   * 
   * @param idWork é o identificador da obra (<code>int</code>)
   * @return <code>String</code> com a mensagem
   */
  static String invalidWorkId(int idWork) {
    return "Número de obra inválido: " + idWork + ".";
  }

  /**
   * <code>requisitada</code> gera a mensagem que indica o período (dias) pelo
   * qual uma obra foi requisitada.
   * 
   * @param idWork é o identificador da obra (<code>int</code>)
   * @param days   an <code>int</code> value
   * @return <code>String</code> com a mensagem
   */
  static String workRequestedForDays(int idWork, int days) {
    return "A obra " + idWork + " foi requisitada por " + days + " dias.";
  }

  static String notEnoughInventory(int idWork, int amount) {
    return "A quantidade de exemplares da obra " + idWork + " não pode ser decrementada em " + amount + " (excederia o inventário).";
   }
}
