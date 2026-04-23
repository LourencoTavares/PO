package bci.app.main;

import bci.app.exception.FileOpenFailedException;
import bci.core.LibraryManager;
import bci.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import java.io.IOException;
import static bci.app.main.Prompt.newSaveAs;

class DoSaveFile extends Command<LibraryManager> {

  DoSaveFile(LibraryManager receiver) {
    super(Label.SAVE_FILE, receiver);
  }

  @Override
  protected final void execute() throws FileOpenFailedException {
  try {
      _receiver.save();
    } catch (MissingFileAssociationException e) {
      try {
        _receiver.saveAs(Form.requestString(newSaveAs()));
      } catch (MissingFileAssociationException | IOException e1) {
        throw new FileOpenFailedException(e1);
      }
    } catch (IOException e) {
      throw new FileOpenFailedException(e);
    }
  }
}