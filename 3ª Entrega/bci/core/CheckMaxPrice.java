package bci.core;

public class CheckMaxPrice extends Rule {
  public CheckMaxPrice(){
    super(6);
  }

  @Override
  protected boolean test(Work w, User u) {
    if (u.getBehavior() == UserBehavior.CUMPRIDOR){
      return true;
    }
    return w.getPrice() <= 2500;
  }
}
