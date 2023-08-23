package com.example.account_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AnalyticsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsConsumer.class);

    private final AtomicInteger createdAccounts = new AtomicInteger(0);
    private final AtomicInteger deletedAccounts = new AtomicInteger(0);

    @KafkaListener(topics = "NewLoanTopic", groupId = "analytics_group")
    public void consume(String message){
        analyzeMessage(message);
        printStats();
    }

    private void analyzeMessage(String message) {
        if (message.contains("Account is deleted.")) {
            deletedAccounts.incrementAndGet();
        } else if (message.contains("New account is saved")) {
            createdAccounts.incrementAndGet();
        }
    }

    private void printStats() {
        LOGGER.info("Created accounts: " + createdAccounts.get());
        LOGGER.info("Deleted accounts: " + deletedAccounts.get());
    }
}