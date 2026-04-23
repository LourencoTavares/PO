package bci.core;

import java.io.*;
import java.util.*;

import bci.core.exception.UnrecognizedEntryException;
import bci.app.exception.NoSuchCreatorException;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.app.exception.UserIsActiveException;


/**
 * Representa a biblioteca e concentra o estado e as operações de gestão
 * de datas, obras (Work), utilizadores (User) e criadores (Creator)
 *
 * Garante a criação incremental de identificadores para obras e utilizadores, e disponibiliza consultas e registos comuns sobre o acervo.
 */
public class Library implements Serializable {

  @Serial
  private static final long serialVersionUID = 202501101348L;

  /** Data corrente da biblioteca (classe de domínio com avanço de dias) */
  private Date _currentDate = new Date(1);
  /** Próximo identificador disponível para obras */
  private int _nextWorkId = 1;
  /** Próximo identificador disponível para utilizadores */
  private int _nextUserId = 1;    

  /** Obras indexadas pelo seu identificador */
  private final Map<Integer, Work> _works = new LinkedHashMap<>();
  /** Utilizadores indexados pelo seu identificador */
  private final Map<Integer, User> _users = new LinkedHashMap<>();
  /** Criadores indexados pelo nome normalizado (case-insensitive) */
  private final Map<String, Creator> _creators = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  /** Lista de requisições (abertas/fechadas) efectuadas na biblioteca */
  private final List<Request> _requests = new ArrayList<>();
  /** Mapa (idUtente -> conjunto de idObra) das obras atualmente requisitadas por cada utente */
  private final Map<Integer, Set<Integer>> _borrowedWorks = new HashMap<>();
  /** Mapa (idObra -> lista de idUtente) dos utentes em lista de espera por uma obra */
  private final Map<Integer, List<Integer>> _waitingUsers = new HashMap<>();
  /** Contador de multas saldadas por utente (para evolução de comportamento) */
  private final Map<Integer, Integer> _paidFinesCount = new HashMap<>();
  /** Observers de disponibilidade por obra (idObra -> lista de observers) */
  private final Map<Integer, java.util.List<WorkObserver>> _availableObservers = new HashMap<>();


  /**
   * Normaliza um nome ou texto removendo espaços nas extremidades
   *
   * @param s texto a normalizar
   * @return cadeia normalizada
   */
  private static String normalizeName(String s) { 
    return s == null ? "" : s.trim(); 
  }

  /**
   * Obtém a data da biblioteca
   *
   * @return data como um inteiro
   */
  int getCurrentDate() {
    return _currentDate.getCurrentDate();
  }

  /**
   * Avança a data da biblioteca
   *
   * @param nDays número de dias a avançar
   * @throws UnrecognizedEntryException se nDays for menor ou igual a zero
   */
  void advanceDays(int nDays) throws UnrecognizedEntryException {
  if (nDays <= 0)
    throw new UnrecognizedEntryException("Invalid number of days: " + nDays);
  _currentDate.advanceDays(nDays);
  }

  /**
   * Regista um novo DVD na biblioteca
   *
   * @param title título do DVD
   * @param director realizador (criador) do DVD
   * @param price preço base
   * @param category categoria da obra
   * @param igac código/identificador IGAC
   * @param nCopies número de cópias iniciais
   * @return Dvd criado e registado
   */
  Dvd registerDvd(String title, Creator director, int price, Category category, String igac, int nCopies) {
    int id = _nextWorkId++;
    Dvd dvd = new Dvd(id, normalizeName(title), price, nCopies, category, nCopies, nCopies, director, normalizeName(igac));
    _works.put(id, dvd);

    return dvd;
  }

  /**
   * Regista (ou obtém) um criador pelo seu nome. Se já existir um Creator com o mesmo nome (ignora maiúsculas/minúsculas), esse objeto é devolvido; caso contrário, é criado e armazenado.
   *
   * @param name nome do criador
   * @return Creator existente ou recém-criado
   */
  Creator registerCreator(String name) {
    String key = normalizeName(name);
    return _creators.computeIfAbsent(key, Creator::new);
  }

  /**
   * Regista um novo livro na biblioteca.
   *
   * @param title título do livro
   * @param authors lista de autores (criadores) do livro
   * @param price preço base
   * @param category categoria da obra
   * @param isbn código ISBN
   * @param nCopies número de cópias iniciais
   * @return Book criado e registado
   */
  Book registerBook(String title, List<Creator> authors, int price, Category category, String isbn, int nCopies) {
    int id = _nextWorkId++;
    Book book = new Book(id, normalizeName(title), price, nCopies, category, nCopies, nCopies, normalizeName(isbn), authors);
    _works.put(id, book);
    return book;
  }

  /**
   * Read text input file at the beginning of the program and populates the
   * the state of this library with the domain entities represented in the text file.
   * 
   * @param filename name of the text input file to process
   * @throws UnrecognizedEntryException if some entry is not correct
   * @throws IOException if there is an IO erro while processing the text file
   **/
  void importFile(String filename) throws UnrecognizedEntryException, IOException  {
    MyParser parser = new MyParser(this);
    parser.parseFile(filename);
  }

  /**
   * Devolve todas as obras ordenadas por um identificador crescente
   *
   * @return lista imutável
   */
  public List<Work> getWorks() {
    List<Work> list = new ArrayList<>(_works.values());
    list.sort(Comparator.comparingInt(Work::getId));
    return list;
  }

  /**
   * Obtém uma obra pelo seu identificador
   *
   * @param id identificador da obra
   * @return a obra correspondente
   * @throws NoSuchWorkException se não existir obra com o identificador indicado
   */
  public Work getWork(int id) throws NoSuchWorkException {
    Work work = _works.get(id);
    if (work == null)
      throw new NoSuchWorkException(id);
    return work;
  }

  /**
   * Cria e regista um novo utilizador
   *
   * @param name  nome do utilizador
   * @param email email do utilizador
   * @return o User criado e registado
   */
  User createUser(String name, String email) {
    int id = _nextUserId++;
    User u = new User(id, normalizeName(name), normalizeName(email));
    _users.put(id, u);
    return u;
  }

 /**
   * Obtém um utilizador pelo seu identificador
   *
   * @param id identificador do utilizador
   * @return o utilizador correspondente
   * @throws NoSuchUserException se não existir utilizador com o identificador indicado
   */
  public User getUser(int id) throws NoSuchUserException {
    User u = _users.get(id);
    if (u == null) throw new NoSuchUserException(id);

    if (hasOverdueRequests(u)) {
      u.suspend();
    } else if (u.getFine() == 0) {
      u.activate();
    }
    return u;
  }

  /**
   * Devolve todos os utilizadores ordenados por nome (case-insensitive) e, em caso de empate, por identificador crescente
   *
   * @return lista imutável dos utilizadores
   */
  public List<User> getUsers() {
    List<User> list = new ArrayList<>(_users.values());
    list.sort(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER).thenComparingInt(User::getId));
    return list;
  }

  /**
   * Obtém as obras associadas a um determinado criador (por nome).
   * Para livros, a verificação é feita sobre a lista de autores; para DVDs,
   * sobre o realizador. A pesquisa é case-insensitive e considera igualdade
   * de referência quando aplicável
   *
   * @param name nome do criador a pesquisar
   * @return lista de obras do criador, ordenada por identificador crescente
   * @throws NoSuchCreatorException se não existir criador com o nome indicado
   */
  public List<Work> getWorksByCreator(String name) throws NoSuchCreatorException {
    String key = normalizeName(name);

    Creator target = _creators.get(key);
    if (target == null) {
      throw new NoSuchCreatorException(name);
    }

    String targetName = target.getName();
    List<Work> result = new ArrayList<>();

    for (Work w : _works.values()) {
      boolean matchesTerm = false;

      if (w instanceof Book b) {
        List<?> authors = b.getAuthors();
        if (authors != null) {
          for (Object a : authors) {
            if (a instanceof Creator c) {
              if (c == target || c.getName().equalsIgnoreCase(targetName)) { 
                matchesTerm = true; break; 
              }
            } else if (a instanceof String s) {
                  if (s.trim().equalsIgnoreCase(targetName)) {
                    matchesTerm = true; 
                    break; 
                    }
              }
          }
        }
      } else if (w instanceof Dvd d) {
          Creator dir = d.getDirector();
          if (dir == target || (dir != null && dir.getName().equalsIgnoreCase(targetName))) {
            matchesTerm = true;
          }
        }

      if (matchesTerm && w.getAvailableCopies() > 0) {
        result.add(w);
      }
    }

    result.sort(Comparator.comparing(Work::getTitle, String.CASE_INSENSITIVE_ORDER).thenComparingInt(Work::getId));
    return result;
  }

  /** @return true se {@code text} contiver {@code qLower} (em minúsculas), ignorando nulls */
  private static boolean containsIgnoreCase(String text, String qLower) {
    if (text == null) {
      return false;
    }
    return text.toLowerCase().contains(qLower);
  }

  /**
   * Altera o inventário de uma obra (disponíveis e total) somando {@code delta}
   * Rejeita operações que resultem em disponibilidades negativas, ou total inferior ao número de exemplares emprestados
   * Remove a obra se o novo total for 0 e não houver exemplares emprestados
   *
   * @param workId id da obra
   * @param delta variação (positiva ou negativa)
   * @return {@code true} se a alteração foi aplicada; {@code false} caso inválida
   * @throws NoSuchWorkException se a obra não existir
   */
  public boolean adjustWorkInventory(int workId, int delta) throws NoSuchWorkException {
    Work w = getWork(workId);

    int available = w.getAvailableCopies();
    int total = w.getNumberOfCopies();
    int borrowed = total - available;

    long newAvailable = (long) available + delta;
    long newTotal = (long) total + delta;

    if (newAvailable < 0){ 
      return false;
    }
    if (newTotal < borrowed){
      return false;
    }

    if (newTotal == 0 && borrowed == 0) {
      removeWorkAndCleanupCreators(w);
      _works.remove(workId);
      return true;
    }

    w.setAvailableCopies((int) newAvailable);
    w.setNumberOfCopies((int) newTotal);
    return true;
  }

  /**
   * Remove uma obra e limpa criadores que fiquem sem qualquer obra associada
   * @param w obra a remover (já validado)
   */
  private void removeWorkAndCleanupCreators(Work w) {
    List<Creator> afetados = new ArrayList<>();
    if (w instanceof Book b) {
      for (Object a : b.getAuthors()) {
        if (a instanceof Creator c) afetados.add(c);
      }
    } else if (w instanceof Dvd d) {
      Creator dir = d.getDirector();
      if (dir != null) afetados.add(dir);
    }

    for (Creator c : afetados) {
      if (!creatorStillUsed(c)) {
        _creators.remove(normalizeName(c.getName()));
      }
    }
  }

  /**
   * Verifica se um criador continua a ter obras associadas no acervo
   * @param c criador
   * @return true se ainda estiver associado a alguma obra, false caso contrário
   */
  private boolean creatorStillUsed(Creator c) {
    String name = c.getName();
    for (Work other : _works.values()) {
      if (other instanceof Book b) {
        for (Object a : b.getAuthors()) {
          if (a instanceof Creator ac && (ac == c || ac.getName().equalsIgnoreCase(name))){
            return true;
          }
          if (a instanceof String s && s.trim().equalsIgnoreCase(name)){
            return true;
          }
        }
      } else if (other instanceof Dvd d) {
        Creator dir = d.getDirector();
        if (dir == c || (dir != null && dir.getName().equalsIgnoreCase(name))){
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Pesquisa por termo (título, autores/realizador) e devolve as obras por id crescente
   * @param term termo de pesquisa (ignora espaços extras)
   * @return lista de obras encontradas
   */
  public List<Work> searchWorks(String term) {
    String q = term == null ? "" : term.trim();
    if (q.isEmpty()){
      return Collections.emptyList();
    }

    String qLower = q.toLowerCase();
    List<Work> result = new ArrayList<>();

    for (Work w : _works.values()) {
      boolean matchesTerm = containsIgnoreCase(w.getTitle(), qLower);

      if (!matchesTerm) {
        if (w instanceof Book b) {
          List<?> authors = b.getAuthors();
          if (authors != null) {
            for (Object a : authors) {
              String name = (a instanceof Creator c) ? c.getName(): (a instanceof String s) ? s: null;
              if (containsIgnoreCase(name, qLower)) {
                matchesTerm = true; break; 
              }
            }
          }
        } else if (w instanceof Dvd d) {
          Creator dir = d.getDirector();
          if (dir != null && containsIgnoreCase(dir.getName(), qLower)) {
            matchesTerm = true;
          }
        }
      }

      if (matchesTerm) {
        result.add(w);
      }
    }

    result.sort(Comparator.comparingInt(Work::getId));
    return result;
  }

  /**
   * Regista que um utente passou a ter a obra requisitada (ligações internas + retira da lista de espera)
   * @param userId id do utente
   * @param workId id da obra
   */
  public void borrowWork(int userId, int workId) {
    _borrowedWorks.computeIfAbsent(userId, k -> new HashSet<>()).add(workId);

    List<Integer> list = _waitingUsers.get(workId);
    if (list != null) {
      list.remove(Integer.valueOf(userId));
      if (list.isEmpty()) {
        _waitingUsers.remove(workId);
      }
    }
    unsubscribeAvailable(workId, userId);
  }

  /**
   * Verifica se o utente tem a obra atualmente requisitada
   * @param userId id do utente
   * @param workId id da obra
   * @return true se a obra estiver no conjunto de requisições ativas do utente
   */
  public boolean hasBorrowed(int userId, int workId) {
      Set<Integer> works = _borrowedWorks.get(userId);
      return works != null && works.contains(workId);
    }

  /**
   * Regista a devolução lógica (remove do conjunto de requisitadas) e emite notificação de disponibilidade através do mecanismo Observer (se aplicável).
   * @param userId id do utente
   * @param workId id da obra
   */
  public void returnWork(int userId, int workId) {
    Set<Integer> works = _borrowedWorks.get(userId);
    if (works != null) {
      works.remove(workId);
    }

    try {
      Work w = getWork(workId);
      notifyAvailable(w);
    } catch (NoSuchWorkException ignore) {
    }
  }

  /**
   * Adiciona um utente à lista de espera para uma determinada obra (sem duplicados)
   * @param workId id da obra
   * @param userId id do utente
   */
  public void addWaitingUser(int workId, int userId) {
    List<Integer> list = _waitingUsers.computeIfAbsent(workId, k -> new ArrayList<>());
    if (!list.contains(userId)) {
      list.add(userId);
    }
  }

  /**
   * Obtém a lista dos utentes em espera por uma obra
   * @param workId id da obra
   * @return lista de ids de utente
   */
  public List<Integer> getWaitingUsers(int workId) {
      return new ArrayList<>(_waitingUsers.getOrDefault(workId, Collections.emptyList()));
    }

  /**
   * Remove toda a lista de espera de uma obra
   * @param workId id da obra
  */
  public void clearWaitingUsers(int workId) {
      _waitingUsers.remove(workId);
    }

  /**
   * Saldar a multa de um utente suspenso, actualizar contadores e (se aplicável) reactivar
   * Pode promover o comportamento do utente para FALTOSO após um número de multas pagas
   *
   * @param userId id do utente
   * @return true se houve alterações de estado
   * @throws NoSuchUserException se o utente não existir
   * @throws UserIsActiveException se o utente estiver ATIVO (não se pode saldar multa ativa)
   */
  public boolean payUserFine(int userId) throws NoSuchUserException, UserIsActiveException {
    User u = getUser(userId);

    if (u.isActive()) {
      throw new UserIsActiveException(userId);
    }

    u.clearFine();

    int n = _paidFinesCount.getOrDefault(userId, 0) + 1;
    _paidFinesCount.put(userId, n);

    if (n >= 3 && u.getBehavior() != UserBehavior.FALTOSO) {
      u.setBehavior(UserBehavior.FALTOSO);
    }

    if (!hasOverdueRequests(u)) {
      u.activate();
    }
    return true;
  }

  /**
   * Indica se o utilizador tem requisições em atraso na data corrente
   * @param user utilizador a analisar
   * @return true se existir pelo menos uma requisição por devolver cujo prazo tenha expirado
   */
  private boolean hasOverdueRequests(User user) {
    for (Request r : _requests) {
      if (r.getUser().getId() == user.getId() && !r.isClosed() && r.getDeadline() < getCurrentDate()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Conta o número de requisições abertas (não devolvidas) de um utente
   * @param userId id do utente
   * @return número de requisições em aberto
   */
  public int countOpenRequests(int userId) {
    int cnt = 0;
    for (Request r : _requests) {
      if (!r.isClosed() && r.getUser().getId() == userId){
        cnt++;
      }
    }
    return cnt;
  }

  /**
   * Cria e regista uma nova requisição aberta
   * @param u utente
   * @param w obra
   * @param currentDay dia corrente
   * @param allowedDays prazo (dias) permitido
   * @return a requisição criada
   */
  public Request openRequest(User u, Work w, int currentDay, int allowedDays) {
    Request r = new Request(u, w, currentDay, allowedDays);
    _requests.add(r);
    return r;
  }

  /**
   * Calcula o prazo de requisição (em dias) para um par utente/obra, com base no comportamento do utente e no número total de exemplares.
   * @param u utente
   * @param w obra
   * @return número de dias de empréstimo
   */
  public int computeLoanDays(User u, Work w) {
    int total = w.getNumberOfCopies();
    int base = (total == 1) ? 3 : (total <= 5 ? 8 : 15);
    return switch (u.getBehavior()) {
      case CUMPRIDOR -> (total == 1) ? 8 : (total <= 5 ? 15 : 30);
      case FALTOSO -> 2;
      default -> base;
    };
  }

  /**
   * Fecha a última requisição aberta (mais recente) para um determinado par (utente, obra), atualiza comportamento, multa e estado activo/suspenso conforme atraso.
   *
   * @param userId id do utente
   * @param workId id da obra
   */
  public void closeOneOpenRequest(int userId, int workId) {
    int today = getCurrentDate();

    for (int i = _requests.size() - 1; i >= 0; i--) {
      Request r = _requests.get(i);
      if (!r.isClosed() && r.getUser().getId() == userId && r.getWork().getId() == workId) {

        r.close(today);

        User u = r.getUser();
        int lateDays = r.daysLate(today);
        boolean onTime = (lateDays == 0);

        u.registerReturn(onTime);

        if (lateDays > 0) {
          int fine = r.computeFine(today, null);
          u.addFine(fine);
          u.suspend();

          if (u.getBehavior() == UserBehavior.CUMPRIDOR) {
            u.setBehavior(UserBehavior.NORMAL);
          }
        }
        break;
      }
    }
  }

  /**
   * Indica se o utente (por id) tem requisições em atraso
   * @param userId id do utente
   * @return true se tiver atrasos
   * @throws NoSuchUserException se o utente não existir
   */
  public boolean userHasOverdues(int userId) throws NoSuchUserException {
    return hasOverdueRequests(getUser(userId));
  }

  /**
   * Aplica suspensão automática ao utente se este tiver requisições em atraso
   * @param userId id do utente
   * @throws NoSuchUserException se o utente não existir
   */
  public void applyAutoSuspensionIfOverdue(int userId) throws NoSuchUserException {
    User u = getUser(userId);
    if (hasOverdueRequests(u)) {
      u.suspend();
    }
  }

  /**
   * Conta quantas obras o utente tem presentemente requisitadas (conjunto em memória)
   * @param userId id do utente
   * @return número de obras requisitadas
   */
  public int countOpenBorrowings(int userId) {
    java.util.Set<Integer> set = _borrowedWorks.get(userId);
    return (set == null) ? 0 : set.size();
  }

  /**
   * (Observer) Subscrição para ser notificado quando a obra voltar a ficar disponível
   * Caso já exista subscrição do mesmo utente para a mesma obra, não duplica
   *
   * @param workId id da obra
   * @param userId id do utente a notificar
   */
  public void subscribeAvailable(int workId, int userId) {
    List<WorkObserver> list = _availableObservers.computeIfAbsent(workId, k -> new ArrayList<>());
    boolean exists = list.stream().anyMatch(o -> o.getUserId() == userId);
    if (!exists) {
      list.add(new NotifyUserObserver(userId));
    }
  }

  /**
   * (Observer) Cancela a subscrição de disponibilidade da obra para o utente indicado
   * @param workId id da obra
   * @param userId id do utente
   */
  public void unsubscribeAvailable(int workId, int userId) {
    List<WorkObserver> list = _availableObservers.get(workId);
    if (list == null){
      return;
    }

    list.removeIf(o -> o.getUserId() == userId);

    if (list.isEmpty()) {
      _availableObservers.remove(workId);
    }
  }

  /**
   * (Observer) Notifica todos os observadores de que a obra ficou disponível
   * Cria snapshot da lista para evitar ConcurrentModification durante callbacks
   *
   * @param w obra que ficou disponível
   */
  private void notifyAvailable(Work w) {
    List<WorkObserver> list = _availableObservers.get(w.getId());
    if (list == null || list.isEmpty()){
      return;
    }

    for (WorkObserver o : new ArrayList<>(list)) {
      o.onAvailable(this, w);
    }
  }

  /**
   * Remove um criador pelo nome apenas se não tiver obras associadas
   * Se o criador não existir ou ainda estiver a ser usado, não faz nada
   *
   * @param name nome do criador
   * @return true se removeu; false se não existia ou ainda tem obras
   */
  public boolean removeCreatorIfOrphan(String name) {
    String key = normalizeName(name);
    Creator c = _creators.get(key);
    if (c == null) {
      return false;
    }
    if (creatorStillUsed(c)) {
      return false;
    }

    _creators.remove(key);
    return true;
  }

  /**
   * Variante por instância. Remove apenas se não tiver obras associadas
   *
   * @param creator instância do criador
   * @return true se removeu; false caso contrário
   */
  public boolean removeCreatorIfOrphan(Creator creator) {
    if (creator == null) return false;
    if (creatorStillUsed(creator)) return false;
    _creators.remove(normalizeName(creator.getName()));
    return true;
  }
}