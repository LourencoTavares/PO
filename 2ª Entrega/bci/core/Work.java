package bci.core;

import java.io.Serializable;

public abstract class Work implements Serializable {
    private static final long serialVersionUID = 202501061210L;


    private int _id;
    private int _price;
    private int _numberOfCopies;
    private String _title;
    private int _totalCopies;
    private int _availableCopies;
    private final Category _category;

    public Work(int id, String title, int price, int numberOfCopies, Category category, int totalCopies, int availableCopies) {
        _id = id;
        _title = (title == null) ? "" : title.trim();
        _price = price;
        _numberOfCopies = numberOfCopies;
        _category = category;
        _totalCopies = totalCopies;
        _availableCopies = availableCopies;
    }

    public int getId() {
        return _id;
    } 

    public int getPrice() {
        return _price;
    }

    public int getNumberOfCopies() {
        return _numberOfCopies;
    }

    public String getTitle() {
        return _title;
    }

    public int getTotalCopies() {
        return _totalCopies;
    }

    public int getAvailableCopies() {
        return _availableCopies;
    }

    public Category getCategory() {
        return _category;
    }

    protected abstract String typeName();

    protected abstract String additionalInfo();

    protected String categoryLabel() {
        return switch (_category) {
            case REFERENCE -> "Referência";
            case FICTION -> "Ficção";
            case SCITECH -> "Técnica e Científica";
        };
    }

    @Override
    public String toString() {
        return _id + " - " + _availableCopies + " de " + _totalCopies + " - " + typeName() + " - " + _title + " - " + _price + " - " + categoryLabel() + " - " + additionalInfo();
    }
}
