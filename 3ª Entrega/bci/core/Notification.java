package bci.core;

import java.io.*;

public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = 202501061216L;

    private final String _message;
    private final int _day;

    public Notification(String message) {
        _message = message;
        _day = -1;
    }

    public Notification(String message, int day) {
        _message = message;
        _day = day;
    }

    public String getMessage(){
        return _message;
    }

    public int getDay(){
        return _day;
    }

    @Override 
    public String toString(){
        return _day >= 1 ? (_day + " - " + _message) : _message;
    }
}