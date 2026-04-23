package bci.core;

public class CheckUserIsActive extends Rule {
  public CheckUserIsActive(){
    super(2);
  }
  
  @Override
  protected boolean test(Work w, User u){
    return u.isActive();
  }
}
