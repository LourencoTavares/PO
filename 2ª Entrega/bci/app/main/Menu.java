package bci.app.main;

import bci.core.LibraryManager;

/** Main menu. */
public class Menu extends pt.tecnico.uilib.menus.Menu {

    public Menu(LibraryManager receiver) {
        super(Label.TITLE, //
                new DoOpenFile(receiver), //
                new DoSaveFile(receiver), //
                new DoDisplayDate(receiver), //
                new DoAdvanceDate(receiver), //
                new DoOpenMenuUsers(receiver), //
                new DoOpenMenuWorks(receiver), //
                new DoOpenMenuRequests(receiver) //
        );
    }
}
