package bci.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
  @Serial
  private static final long serialVersionUID = 202501101410L;

  private int _id;
  private boolean _isActive = true;
  private String _name;
  private String _email;
  private UserBehavior _behavior = UserBehavior.NORMAL;
  private int _fine = 0;
  private final List<Notification> _notifications = new ArrayList<>();
  private int _onTime = 0;
  private int _late = 0; 

  private static final int PROMOTE_ON_TIME_STREAK = 5;

  private int _lateCountSinceSuspension = 0;

  public User(int id, String name, String email) {
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

  public void setBehavior(UserBehavior b){ 
    _behavior = b; 
  }

  public boolean isActive(){
    return _isActive; 
  }

  public void activate(){ 
    _isActive = true; 
  }

  public void suspend(){
     _isActive = false; 
  }

  public void setActive(boolean active){
    _isActive = active; 
  }

  public int getFine(){ 
    return _fine; 
  }

  public void addFine(int euros){
    if (euros > 0) _fine += euros; 
  }

  public void clearFine(){ 
    _fine = 0; 
  }

  public void addNotification(Notification n) {
    if (n == null){
      return;
    }

    String novo = n.toString();
    for (Notification ex : _notifications) {
      if (ex.toString().equals(novo)){
        return;
      }
    }
    _notifications.add(n);
  }

  public List<Notification> getNotifications(){ 
    return List.copyOf(_notifications); 
  }

  public void clearNotifications(){
     _notifications.clear(); 
  }

  public void registerReturn(boolean onTime) {
    if (onTime) {
      _onTime++;
      _late = 0;

      if (_behavior == UserBehavior.FALTOSO) {
        if (_onTime >= 3) {
          _behavior = UserBehavior.NORMAL;
        }
      } else if (_behavior == UserBehavior.NORMAL) {
        if (_onTime >= PROMOTE_ON_TIME_STREAK) {
          _behavior = UserBehavior.CUMPRIDOR;
        }
      }
    } else {
      _late++;
      _onTime = 0;
      _lateCountSinceSuspension++;
    }
  }

  public int getLateCountSinceSuspension(){
    return _lateCountSinceSuspension; 
  }

  public void clearLateSinceSuspension(){ 
    _lateCountSinceSuspension = 0; 
  }

  public boolean hadLateSinceSuspension(){
    return _lateCountSinceSuspension > 0; 
  } 


  @Override
  public String toString() {
    String state = _isActive ? "ACTIVO" : "SUSPENSO";
    if (_isActive) {
      return _id + " - " + _name + " - " + _email + " - " + _behavior + " - " + state;
    } else {
      return _id + " - " + _name + " - " + _email + " - " + _behavior + " - " + state + " - EUR " + _fine;
    }
  }
}