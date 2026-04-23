package bci.core;

import bci.app.exception.BorrowingRuleFailedException;
import bci.app.exception.NoSuchCreatorException;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.app.exception.UserIsActiveException;
import bci.app.exception.UserRegistrationFailedException;
import bci.core.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryManager {

  /** Entidade de domínio com o estado da biblioteca. */
  private Library _library;
  /** Nome do ficheiro associado ao save/load da biblioteca. */
  private String _filename;
  /** Indica se existem alterações não gravadas. */
  private boolean _dirty = false;
  /** Cadeia de regras de validação para requisições (Strategy/Chain-of-Responsibility). */
  private List<Rule> _rules;

  /**
   * Construtor: cria uma biblioteca vazia e inicializa a cadeia de regras.
   * Novas regras podem ser acrescentadas aqui sem alterar a lógica restante.
   */
  public LibraryManager(){ 
    _library = new Library();
    _rules = Arrays.asList(
    new CheckRequestTwice(_library),   // 1
    new CheckUserIsActive(),           // 2
    new CheckWorkAvailable(),          // 3
    new CheckMaxOpenRequests(_library),// 4
    new CheckNotReference(),           // 5
    new CheckMaxPrice()                // 6
  );
  }

  /**
   * Saves the serialized application's state into the file associated to the current library
   *
   * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
   * @throws MissingFileAssociationException if the current library does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
    if(_filename == null){
      throw new MissingFileAssociationException();
    }

    try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))){
      out.writeObject(_library);
      _dirty = false;
    }
  }

  /**
   * Saves the serialized application's state into the specified file. The current library is
   * associated to this file.
   *
   * @param filename the name of the file.
   * @throws FileNotFoundException if for some reason the file cannot be created or opened.
   * @throws MissingFileAssociationException if the current library does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
    _filename = filename;
    save();
  }

  /**
   * Loads the previously serialized application's state as set it as the current library.
   *
   * @param filename name of the file containing the serialized application's state
   *        to load.
   * @throws UnavailableFileException if the specified file does not exist or there is
   *         an error while processing this file.
   **/
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException {
    _filename = filename;
    try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
      this._library = (Library) in.readObject();
      _dirty = false;
    } catch (IOException e) {
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * Read text input file and initializes the current library (which should be empty)
   * with the domain entities representeed in the import file.
   *
   * @param datafile name of the text input file
   * @throws ImportFileException if some error happens during the processing of the
   * import file.
   **/
  public void importFile(String filename) throws ImportFileException {
    try {
      _library.importFile(filename);
      _dirty = false;
    } catch (IOException | UnrecognizedEntryException e) {
      throw new ImportFileException(filename, e);
    }
  }

  /**
   * Altera os exemplares disponíveis/total da obra somando {@code amount}
   *
   * @param id id da obra
   * @param amount variação (positiva/negativa)
   * @return true se aplicou a alteração, false se inválida
   * @throws NoSuchWorkException se a obra não existir
  */
  public boolean changeWorkAvailable(int id, int amount) throws NoSuchWorkException {
    boolean ok = _library.adjustWorkInventory(id, amount);
    if (ok){
      _dirty = true;
    }
    return ok;
  }

  /** Pesquisa obras por termo. */
  public List<Work> searchWorks(String term) {
    return _library.searchWorks(term);
  }

  /** @return data corrente da biblioteca. */
  public int getCurrentDate() {
    return _library.getCurrentDate();
  }

  /**
   * Avança a data um certo número de dias (positivos).
   * @param nDays número de dias a avançar
   * @throws UnrecognizedEntryException se for ≤ 0
   */
  public void advanceDays(int nDays) throws UnrecognizedEntryException {
    if (nDays > 0) {
      _library.advanceDays(nDays);
      _dirty = true;
    }
  }

  /**
   * Regista um novo utente, validando nome/email.
   *
   * @param name nome
   * @param email email
   * @return id do utente criado
   * @throws UserRegistrationFailedException se nome/email forem vazios
   */
  public int registerUser(String name, String email) throws UserRegistrationFailedException {
    String n = name == null ? "" : name.trim();
    String e = email == null ? "" : email.trim();
    if (n.isEmpty() || e.isEmpty()){
      throw new UserRegistrationFailedException(n, e);
    }
    int id = _library.createUser(n, e).getId();
    _dirty = true;
    return id;
  }

  /** Obtém um utente por id. */
  public User getUser(int id) throws NoSuchUserException {
    return _library.getUser(id);
  }

  /** Lista ordenada de utentes */
  public List<User> getUsers() {
    return new ArrayList<>(_library.getUsers());
  }
  
  /** Obtém uma obra por id */
  public Work getWork(int id) throws NoSuchWorkException {
    return _library.getWork(id);
  }

  /** Lista de obras por id crescente */
  public List<Work> getWorks() {
    return new ArrayList<>(_library.getWorks());
  }

  /** Lista de obras de um criador (apenas disponíveis) */
  public List<Work> getWorksByCreator(String name) throws NoSuchCreatorException {
    return new ArrayList<>(_library.getWorksByCreator(name));
  }

  /** @return true se existirem alterações por gravar */
  public boolean isDirty() {
    return _dirty;
  }

  /**
   * Regista internamente que o utente passou a ter a obra requisitada
   * (Não mexe em contadores da obra; isso é feito na camada de UI/comando)
   */
  public void borrowWork(int userId, int workId) {
    _library.borrowWork(userId, workId);
    _dirty = true;
  }

  /** Verifica se o utente tem a obra requisitada de momento */
  public boolean hasBorrowed(int userId, int workId) {
    return _library.hasBorrowed(userId, workId);
  }

  /**
   * Regista a devolução lógica e desencadeia notificações de disponibilidade (Observer)
   * (A alteração de cópias disponíveis é tratada no comando de devolução)
   */
  public void returnWork(int userId, int workId) {
    _library.returnWork(userId, workId);
    _dirty = true;
  }

  /** Adiciona o utente à lista de espera da obra (sem duplicados) */
  public void addWaitingUser(int workId, int userId) {
    _library.addWaitingUser(workId, userId);
    _dirty = true;
  }

  /** Devolve a lista de ids de utentes em espera por uma obra */
  public List<Integer> getWaitingUsers(int workId) {
    return _library.getWaitingUsers(workId);
  }

  /** Limpa a lista de espera da obra */
  public void clearWaitingUsers(int workId) {
    _library.clearWaitingUsers(workId);
    _dirty = true;
  }

  /**
   * Saldar multa de um utente suspenso; pode reativar o utente se não tiver atrasos
   *
   * @param userId id do utente
   * @return true se houve alterações de estado
   * @throws NoSuchUserException se o utente não existir
   * @throws UserIsActiveException se o utente estiver ACTIVO
   */
  public boolean payUserFine(int userId) throws NoSuchUserException, UserIsActiveException {
    boolean changed = _library.payUserFine(userId);
    if (changed){
      _dirty = true;
    }
    return changed;
  }

  /**
   * Verifica todas as regras configuradas para uma requisição (ordem definida no construtor)
   *
   * @param w obra a requisitar
   * @param u utente que requisita
   * @throws BorrowingRuleFailedException se alguma regra falhar
   */
  public void checkAllRules(Work w, User u) throws BorrowingRuleFailedException {
    for (Rule r : _rules){
      r.check(w, u);
    }
  }

  /** Calcula prazo de empréstimo em dias para (utente, obra) */
  public int computeLoanDays(User u, Work w){
    return _library.computeLoanDays(u, w);
  }

  /** Número de obras actualmente requisitadas pelo utente */
  public int countOpenBorrowings(int userId){
    return _library.countOpenBorrowings(userId);
  }

  /** Número de requisições abertas (não devolvidas) do utente */
  public int countOpenRequests(int userId){
    return _library.countOpenRequests(userId);
  }

  /**
   * Cria e regista uma nova requisição (aberta)
   *
   * @param u utente
   * @param w obra
   * @param currentDay dia corrente
   * @param allowedDays prazo concedido
   * @return a {@link Request} criada
   */
  public Request createRequest(User u, Work w, int currentDay, int allowedDays) {
    return _library.openRequest(u, w, currentDay, allowedDays);
  }

  /**
   * Fecha a última requisição aberta desse par (utente, obra), atualizando multas/estado
   */
  public void closeOneOpenRequest(int userId, int workId) {
    _library.closeOneOpenRequest(userId, workId);
  }

  /**
   * Aplica suspensão automática a um utente que tenha requisições em atraso
   * @throws NoSuchUserException se o utente não existir
   */
  public void autoSuspendIfOverdue(int userId) throws NoSuchUserException {
    _library.applyAutoSuspensionIfOverdue(userId);
    _dirty = true;
  }

/**
 * Remove um criador se não tiver obras associadas.
 *
 * @param name nome do criador
 * @return true se removeu; false caso contrário
*/
public boolean removeCreatorIfOrphan(String name) {
  boolean removed = _library.removeCreatorIfOrphan(name);
    if (removed){
      _dirty = true;
    }
  return removed;
}
}