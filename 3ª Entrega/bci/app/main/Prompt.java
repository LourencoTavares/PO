package bci.app.main;

/** Messages for menu interactions. */
interface Prompt {

  /**
   * @return string with prompt for filename to open.
   */
  static String openFile() {
    return "Ficheiro a abrir: ";
  }

  /**
   * @return string confirming that user wants to save.
   */
  static String saveBeforeExit() {
    return "Guardar antes de fechar? ";
  }

  /**
   * @return string asking for a filename.
   */
  static String saveAs() {
    return "Guardar ficheiro como: ";
  }

  /**
   * @return string with a warning and a question.
   */
  static String newSaveAs() {
    return "Ficheiro sem nome. " + saveAs();
  }

  /**
   * <code>daysToAdvance</code> gera a mensagem que indica que é necessário
   * fornecer um número de dias ao sistema, para fazer avançar a data. O valor da
   * data é medido em dias e é um número inteiro.
   * 
   * @return <code>String</code> com a mensagem
   */
  static String daysToAdvance() {
    return "Introduza número de dias a avançar: ";
  }
}
