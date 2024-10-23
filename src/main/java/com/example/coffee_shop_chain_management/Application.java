package com.example.coffee_shop_chain_management;

import com.example.coffee_shop_chain_management.telegram_bot.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private NotificationBot bot;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		startBot();
	}

	public void startBot() throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

		try {
			telegramBotsApi.registerBot(bot); // Đăng ký bot một lần duy nhất
		} catch (TelegramApiException e) {
			e.printStackTrace();
			System.err.println("Error when registering bot: " + e.getMessage());
		}
	}
}
