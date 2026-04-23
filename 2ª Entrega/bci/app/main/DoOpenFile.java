package bci.app.main;

import bci.core.LibraryManager;
import bci.app.exception.FileOpenFailedException;
import bci.core.exception.UnavailableFileException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenFile extends Command<LibraryManager> {

  DoOpenFile(LibraryManager receiver) {
    super(Label.OPEN_FILE, receiver);
    addStringField("fileName", Prompt.openFile());
  }

  @Override
  protected final void execute() throws CommandException {
    String filename = stringField("fileName");
    try {
      _receiver.load(filename);
    } catch (UnavailableFileException | ClassNotFoundException e) {
      throw new FileOpenFailedException(e);
    }
  }
}
