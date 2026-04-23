package bci.core;


import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Creator implements Serializable {
    @Serial
    private static final long serialVersionUID = 202501061200L;

    private String _name;
    private final Set<Work> _works = new LinkedHashSet<>();

    Creator(String name) {
        String cleanedName;

        if (name == null) {
            cleanedName = "";
        } else {
            cleanedName = name.trim();
        }

        _name = cleanedName;
    }

    String getName() {
        return _name;
    }

    boolean addWork(Work w) {
        if (w == null) {
            return false;
        }
        return _works.add(w);
    }

    boolean removeWork(Work w) {
        if (w == null) {
            return false;
        }
        return _works.remove(w);
    }

    Collection<Work> works() {
        return Collections.unmodifiableSet(_works);
    }

    boolean hasNoWork() {
        return _works.isEmpty();
    }

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Creator c)) {
            return false;
        }

        return Objects.equals(_name, c._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name);
    }
}