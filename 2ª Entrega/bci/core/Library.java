package bci.core;

import java.io.*;
import java.util.*;

import bci.core.exception.UnrecognizedEntryException;
import bci.app.exception.NoSuchCreatorException;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;


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
      if (w instanceof Book b) {
        List<?> authors = b.getAuthors();
        if (authors != null) {
          boolean matches = false;
          for (Object a : authors) {
            if (a instanceof Creator c) {
              if (c == target || c.getName().equalsIgnoreCase(targetName)) { matches = true; break; }
            } else if (a instanceof String s) {
              if (s.trim().equalsIgnoreCase(targetName)) { matches = true; break; }
            }
          }
          if (matches) result.add(w);
        }
      } else if (w instanceof Dvd d) {
        Creator dir = d.getDirector(); 
        if (dir == target || (dir != null && dir.getName().equalsIgnoreCase(targetName))) {
          result.add(w);
        }
      }
    }

    result.sort(Comparator.comparingInt(Work::getId));
    return result;
  }
}
