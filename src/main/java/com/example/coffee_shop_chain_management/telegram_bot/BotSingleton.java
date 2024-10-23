package com.example.coffee_shop_chain_management.telegram_bot;

public class BotSingleton {
    private static NotificationBot instance;

    private BotSingleton() {}

    public static NotificationBot getInstance() {
        if (instance == null) {
            instance = new NotificationBot();
        }
        return instance;
    }
}
