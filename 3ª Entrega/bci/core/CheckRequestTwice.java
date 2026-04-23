package bci.core;

public class CheckRequestTwice extends Rule {
  private final Library _lib;

  public CheckRequestTwice(Library lib){
    super(1); 
    _lib = lib;
  }
  
  @Override
  protected boolean test(Work w, User u){
    return !_lib.hasBorrowed(u.getId(), w.getId());
  }
}
