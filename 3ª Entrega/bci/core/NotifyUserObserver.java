package bci.core;

import java.io.*;


public class NotifyUserObserver implements WorkObserver {
    @Serial
    private static final long serialVersionUID = 202501101518L;
    
    private final int _userId;

    public NotifyUserObserver(int userId) {
        _userId = userId;
    }

    @Override
    public int getUserId() {
        return _userId;
    }

    @Override
    public void onAvailable(Library lib, Work w) {
        try {
            User u = lib.getUser(_userId);
            u.addNotification(new Notification(buildAvailabilityLine(w)));
        } catch (Exception ignore) {
        }
    }

    private String buildAvailabilityLine(Work w) {
        int disp = w.getAvailableCopies();
        int total = w.getNumberOfCopies();

        String tipo = (w instanceof Dvd) ? "DVD" : "Livro";
        String categoria = catPT(w.getCategory());

        String code = "";
        String autorOuRealizador = "";

        if (w instanceof Dvd d) {
            code = d.getIgac();
            Creator dir = d.getDirector();
            if (dir != null){
                autorOuRealizador = dir.getName();
            }
        } else if (w instanceof Book b) {
            code = b.getIsbn();
            if (b.getAuthors() != null && !b.getAuthors().isEmpty()) {
                Object a0 = b.getAuthors().get(0);
                autorOuRealizador = (a0 instanceof Creator c) ? c.getName() : a0.toString();
            }
        }

        return "DISPONIBILIDADE: " + w.getId() + " - " + disp + " de " + total + " - " + tipo + " - " + w.getTitle() + " - " + w.getPrice() + " - " + categoria + " - " + autorOuRealizador + " - " + code;
    }

    private String catPT(Category c) {
        switch (c) {
            case SCITECH:   
                return "Técnica e Científica";
            case FICTION:   
                return "Ficção";
            case REFERENCE: 
                return "Referência";
            default:        
                return c.toString();
        }
    }
}
