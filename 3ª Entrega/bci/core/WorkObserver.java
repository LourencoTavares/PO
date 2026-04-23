package bci.core;

public interface WorkObserver {
  int getUserId();

  void onAvailable(Library lib, Work work);

  default void onBorrow(Library lib, Work work) {}
}
