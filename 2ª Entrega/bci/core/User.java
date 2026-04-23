package bci.core;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 202501101410L;

    private int _id;
    private boolean _isActive = true;
    private String _name;
    private String _email;
    private UserBehavior _behavior = UserBehavior.NORMAL;


    public User (int id , String name, String email){
        _id = id;
        _name = name;
        _email = email;
    } 

    public int getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public String getEmail(){
        return _email;
    }

    public UserBehavior getBehavior(){
        return _behavior;
    }

    public boolean isActive(){
        return _isActive;
    }

    public String toString() {
        return _id + " - " + _name + " - " + _email + " - " + _behavior + " - " + (_isActive ? "ACTIVO" : "SUSPENSO");
  }
}