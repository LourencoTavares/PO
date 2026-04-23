package bci.core;

import bci.app.exception.BorrowingRuleFailedException;
import java.io.*;

public abstract class Rule implements Serializable {
  @Serial
  private static final long serialVersionUID = 202501061214L;

  private final int _id;

  protected Rule(int id){ 
    _id = id;
  }

  public final void check(Work w, User u) throws BorrowingRuleFailedException {
    if (!test(w, u)){
      throw new BorrowingRuleFailedException(u.getId(), w.getId(), _id);
    }
  }
  
  protected abstract boolean test(Work w, User u);
}

