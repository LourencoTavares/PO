package bci.core;

public class CheckMaxOpenRequests extends Rule {
  private final Library _lib;

  public CheckMaxOpenRequests(Library lib){
    super(4); _lib = lib;
  }
  
  @Override
  protected boolean test(Work w, User u){
    int open = _lib.countOpenRequests(u.getId());
    int cap = switch (u.getBehavior()){
      case CUMPRIDOR -> 5;
      case FALTOSO -> 1;
      default -> 3;
    };
    return open < cap;
  }
}
