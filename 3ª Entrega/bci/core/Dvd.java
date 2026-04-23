package bci.core;

import java.io.*;


public class Dvd extends Work{
    @Serial
    private static final long serialVersionUID = 202501061251L;
;

    private final Creator _director;
    private final String _igac;

    public Dvd(int id, String title, int price, int numberOfCopies, Category category, int totalCopies, int availableCopies, Creator director, String igac){
        super(id, title, price, numberOfCopies, category, totalCopies, availableCopies);
        _director = director;
        _igac = igac;
        this._availableCopies = numberOfCopies;
        this._numberOfCopies  = numberOfCopies;
    }

    public Creator getDirector() {
        return _director;
    }

    public String getIgac() {
        return _igac;
    }

    @Override
    protected String typeName() {
        return "DVD";
    }

    @Override
    protected String additionalInfo() {
        return _director + " - " + _igac;
    }
}

