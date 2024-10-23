package com.example.coffee_shop_chain_management.telegram_bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainBotTelegram {
    public void startBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        NotificationBot bot = new NotificationBot();

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.err.println("Error when registering bot: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MainBotTelegram mainBotTelegram = new MainBotTelegram();
        try {
            mainBotTelegram.startBot();
        } catch (TelegramApiException e) {
            System.err.println("Error when starting bot: " + e.getMessage());
        }
    }
}
