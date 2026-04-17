package com.pms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleNotificationObserver implements NotificationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleNotificationObserver.class);

    @Override
    public void update(String message) {
        LOGGER.info("Notification: {}", message);
    }
}
