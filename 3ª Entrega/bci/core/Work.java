package bci.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public abstract class Work implements Serializable {
    @Serial
    private static final long serialVersionUID = 202501061210L;


    private int _id;
    private int _price;
    private String _title;
    private int _totalCopies;
    protected int _availableCopies;
    protected int _numberOfCopies;
    private final Category _category;
    private List<WorkObserver> _observers;

    public Work(int id, String title, int price, int numberOfCopies, Category category, int totalCopies, int availableCopies) {
        _id = id;
        _title = (title == null) ? "" : title.trim();
        _price = price;
        _category = category;
        _totalCopies = totalCopies;
        _numberOfCopies = numberOfCopies;
        _availableCopies = availableCopies;
        _observers = new ArrayList<>();
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

    public void setAvailableCopies(int v){ 
        _availableCopies = v;
    }

    public void setNumberOfCopies(int v){
        _numberOfCopies  = v;
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

    public boolean changeAvailableCopies(int delta) {
        long next = (long) _availableCopies + delta;
        if (next < 0 || next > Integer.MAX_VALUE){
            return false;
        }
        _availableCopies = (int) next;
        return true;
    }

    @Override
    public String toString() {
        return _id + " - " + getAvailableCopies() + " de " + getNumberOfCopies() + " - " + typeName() + " - " + _title + " - " + _price + " - " + categoryLabel() + " - " + additionalInfo();
    }

    public void addObserver(WorkObserver observer) {
        _observers.add(observer);
    }

    public void removeObserver(WorkObserver observer) {
        _observers.remove(observer);
    }
}
