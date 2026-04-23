package bci.app.main;

import bci.core.LibraryManager;
import bci.app.exception.FileOpenFailedException;
import bci.core.exception.UnavailableFileException;
import bci.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
import java.io.IOException;
import static bci.app.main.Prompt.openFile;
import static bci.app.main.Prompt.newSaveAs;
import static bci.app.main.Prompt.saveBeforeExit;

class DoOpenFile extends Command<LibraryManager> {

   DoOpenFile(LibraryManager receiver) {
     super(Label.OPEN_FILE, receiver);
   }

@Override
protected final void execute() throws CommandException {
  if (_receiver.isDirty()) {
    if (Form.confirm(saveBeforeExit())) {
      try {
        _receiver.save();
      } catch (MissingFileAssociationException e) {
        String saveAsName = Form.requestString(newSaveAs());
        try {
          _receiver.saveAs(saveAsName);
        } catch (MissingFileAssociationException | IOException ex) {
          throw new FileOpenFailedException(ex);
        }
      } catch (IOException e) {
        throw new FileOpenFailedException(e);
      }
    }
  }

  String filename = Form.requestString(openFile());
  try {
    _receiver.load(filename);
  } catch (UnavailableFileException | ClassNotFoundException e) {
    throw new FileOpenFailedException(e);
  }
}
}