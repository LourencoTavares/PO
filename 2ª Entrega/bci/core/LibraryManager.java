package bci.core;

import bci.app.exception.NoSuchCreatorException;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.app.exception.UserRegistrationFailedException;
import bci.core.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
  
  private Library _library;
  private String _filename;

  public LibraryManager(){ 
    _library = new Library();
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
    } catch (IOException | UnrecognizedEntryException e) {
      throw new ImportFileException(filename, e);
    }
  }

  public int getCurrentDate() {
    return _library.getCurrentDate();
  }

  public void advanceDays(int nDays) throws UnrecognizedEntryException {
    if (nDays > 0) {
      _library.advanceDays(nDays);
    }
  }

  public int registerUser(String name, String email) throws UserRegistrationFailedException {
    String n = name == null ? "" : name.trim();
    String e = email == null ? "" : email.trim();
    if (n.isEmpty() || e.isEmpty()){
      throw new UserRegistrationFailedException(n, e);
    }
    return _library.createUser(n, e).getId();
  }

  public User getUser(int id) throws NoSuchUserException {
    return _library.getUser(id);
  }

  public List<User> getUsers() {
    return new ArrayList<>(_library.getUsers());
  }
  
  public Work getWork(int id) throws NoSuchWorkException {
    return _library.getWork(id);
  }

  public List<Work> getWorks() {
    return new ArrayList<>(_library.getWorks());
  }

  public List<Work> getWorksByCreator(String name) throws NoSuchCreatorException {
    return new ArrayList<>(_library.getWorksByCreator(name));
  }
}

