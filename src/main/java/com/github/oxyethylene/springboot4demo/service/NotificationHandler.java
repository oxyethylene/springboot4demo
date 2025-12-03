package com.github.oxyethylene.springboot4demo.service;

import com.github.oxyethylene.springboot4demo.common.strategy.StrategyHandler;
import com.github.oxyethylene.springboot4demo.common.strategy.StrategyMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * Example: Notification sending with annotation-based strategy
 *
 * This demonstrates how simple it is to add a new routing scenario:
 * 1. Create a @StrategyHandler class
 * 2. Add @StrategyMapping methods
 * 3. Use dispatcher.dispatch() in your controller
 *
 * That's it! No interfaces, no factories, no separate classes!
 */
@Slf4j
@StrategyHandler
public class NotificationHandler {

    @StrategyMapping("EMAIL")
    public void sendEmail(String recipient, String message) {
        log.info("Sending email to {}: {}", recipient, message);
        // Email sending logic
        // - Connect to SMTP server
        // - Format HTML email
        // - Send with attachments
    }

    @StrategyMapping("SMS")
    public void sendSms(String recipient, String message) {
        log.info("Sending SMS to {}: {}", recipient, message);
        // SMS sending logic
        // - Connect to SMS gateway
        // - Handle character limits
        // - Track delivery status
    }

    @StrategyMapping("PUSH")
    public void sendPushNotification(String recipient, String message) {
        log.info("Sending push notification to {}: {}", recipient, message);
        // Push notification logic
        // - Connect to FCM/APNS
        // - Handle device tokens
        // - Track open rates
    }

    @StrategyMapping("WEBHOOK")
    public void sendWebhook(String recipient, String message) {
        log.info("Sending webhook to {}: {}", recipient, message);
        // Webhook logic
        // - HTTP POST to endpoint
        // - Handle retries
        // - Verify signatures
    }

    /**
     * Example: One method handling multiple similar keys
     */
    @StrategyMapping({"SLACK", "DISCORD", "TEAMS"})
    public void sendToChat(String recipient, String message) {
        log.info("Sending chat message to {} via {}: {}", recipient, "chat platform", message);
        // Chat platform logic
        // - Format for specific platform
        // - Handle mentions and formatting
        // - Post to channel/DM
    }
}
