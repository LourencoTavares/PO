package bci.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.util.List;
import java.util.ArrayList;

import bci.core.exception.UnrecognizedEntryException;

class MyParser {
  private Library _library;

  MyParser(Library lib) {
    _library = lib;
  }

  void parseFile(String filename) throws IOException, UnrecognizedEntryException {
    String line;

    try (BufferedReader in = new BufferedReader(new FileReader(filename));) {
      while ((line = in.readLine()) != null)
        parseLine(line);
    }
  }

  private void parseLine(String line) throws UnrecognizedEntryException {
    String[] components = line.split(":");

    switch (components[0]) {
      case "USER":
        parseUser(components, line);
        break;

      case "DVD":
        parseDvd(components, line);
        break;

      case "BOOK":
        parseBook(components, line);
        break;

      default:
        throw new UnrecognizedEntryException("Tipo inválido " + components[0] + " na linha " + line);
    }
  }

  private void parseUser(String[] components, String line) throws UnrecognizedEntryException {
    try {
      if (components.length != 3)
        throw new UnrecognizedEntryException ("Número inválido de campos (3) na descrição de um utente: " + line);

      _library.createUser(components[1], components[2]);
    } catch (Exception e) {
      throw new UnrecognizedEntryException (line);
    }
  }

  private void parseDvd(String[] components, String line) throws UnrecognizedEntryException {
    if (components.length != 7)
      throw new UnrecognizedEntryException ("Número inválido de campos (7) na descrição de um DVD: " + line);

    String tittle = components[1].trim();
    int price = Integer.parseInt(components[3]);
    int nCopies = Integer.parseInt(components[6]);
    String igacNumber = components[5].trim();
    Category category = Category.valueOf(components[4]);
    Creator creator = _library.registerCreator(components[2].trim());

    _library.registerDvd(tittle, creator, price, category, igacNumber, nCopies);
  }
  
  private void parseBook(String[] components, String line) throws UnrecognizedEntryException {
    if (components.length != 7)
      throw new UnrecognizedEntryException ("Número inválido de campos (7) na descrição de um Book: " + line);
    
    String tittle = components[1].trim();
    int price = Integer.parseInt(components[3]);
    int nCopies = Integer.parseInt(components[6]);
    Category category = Category.valueOf(components[4]);
    String isbn = components[5].trim();
    List<Creator> creators = new ArrayList<>();
    for (String name : components[2].split(","))
      creators.add(_library.registerCreator(name.trim()));

    _library.registerBook(tittle, creators, price, category, isbn, nCopies);
  }
}