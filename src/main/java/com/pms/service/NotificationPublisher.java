package com.pms.service;

public interface NotificationPublisher {
    void registerObserver(NotificationObserver observer);
    void removeObserver(NotificationObserver observer);
    void notifyObservers(String message);
}
