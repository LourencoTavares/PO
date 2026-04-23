package bci.core;

public class CheckNotReference extends Rule {
  public CheckNotReference(){
    super(5);
  }

  @Override
  protected boolean test(Work w, User u){
    return w.getCategory() != Category.REFERENCE;
  }
}
