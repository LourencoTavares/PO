package bci.core;

public class CheckWorkAvailable extends Rule{
  public CheckWorkAvailable(){
    super(3);
  }
  
  @Override 
  protected boolean test(Work w, User u){
    return w.getAvailableCopies() > 0;
  }
}
