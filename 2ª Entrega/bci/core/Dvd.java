package bci.core;

public class Dvd extends Work{
    private static final long serialVersionUID = 1L;

    private final Creator _director;
    private final String _igac;

    public Dvd(int id, String title, int price, int numberOfCopies, Category category, int totalCopies, int availableCopies, Creator director, String igac){
        super(id, title, price, numberOfCopies, category, totalCopies, availableCopies);
        _director = director;
        _igac = igac;
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

