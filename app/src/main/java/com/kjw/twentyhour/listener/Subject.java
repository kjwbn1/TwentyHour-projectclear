package com.kjw.twentyhour.listener;

public interface Subject {
    void registerObserver(OnStoreDataChangeListener o);
    void removeObserver(OnStoreDataChangeListener o);
    void notifyObservers();
}
