package bci.core;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Book extends Work{
    @Serial
    private static final long serialVersionUID = 202501061223L;

    private final List<String> _authors;
    private final String _isbn;

    public Book(int id, String title, int price, int numberOfCopies, Category category, int totalCopies, int availableCopies, String isbn, List<Creator> authors) {
        super(id, title, price, numberOfCopies, category, totalCopies, availableCopies);
        _authors = authors.stream().map(a -> a == null ? "" : a.toString().trim()).filter(a -> !a.isEmpty()).collect(Collectors.toList());
        _isbn = (isbn == null) ? "" : isbn.trim();
        this._availableCopies = numberOfCopies;
        this._numberOfCopies  = numberOfCopies;
    }

    public List<String> getAuthors() {
        return _authors;
    }

    public String getIsbn() {
        return _isbn;
    }

    @Override
    protected String typeName() {
        return "Livro";
    }

    @Override
    protected String additionalInfo() {
        String authors = _authors.stream().collect(Collectors.joining("; "));
        return authors + " - " + _isbn;
    }

}
