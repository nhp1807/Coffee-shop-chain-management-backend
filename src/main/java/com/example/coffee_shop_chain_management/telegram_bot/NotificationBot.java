package com.example.coffee_shop_chain_management.telegram_bot;

import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.enums.OTPType;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class NotificationBot extends TelegramLongPollingBot {
    private String TELEGRAM_BOT_USERNAME;
    private String TELEGRAM_BOT_TOKEN;
    private Map<String, String> userCommands = new HashMap<>();
    private Map<String, String> pendingUserVerification = new HashMap<>();
    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private SendOTP sendOTP;

    public NotificationBot() {
        readPropertiesFile();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (userCommands.containsKey(chatId) && userCommands.get(chatId).equals("VERIFY")) {
                verifyOtp(Integer.parseInt(messageText), chatId);  // Kiểm tra OTP
            } else if (userCommands.containsKey(chatId) && userCommands.get(chatId).equals("IDENTIFIER")) {
                int otpCode = sendOTP.generateOTP();
                OTP otp = new OTP(otpCode, messageText, OTPType.REGISTER);
                otpRepository.save(otp);
                sendOTP.sendMail("Your OTP is: " + otpCode, messageText);
                requestOtpForIdentifier(messageText, chatId); // Yêu cầu OTP dựa trên định danh
            } else {
                checkInput(messageText, chatId);  // Xử lý các lệnh khác
            }
        }
    }

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TELEGRAM_BOT_TOKEN;
    }

    public String showHelp() {
        return "This is help function";
    }

    public void sendMessage(String text, String chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void checkInput(String messageText, String chatId) {
        if (messageText.equals("/start")) {
            sendMessage("Welcome to Coffee Shop Chain Management Bot!", chatId);
            sendMessage("Please type /help to see available commands.", chatId);
        } else if (messageText.equals("/help")) {
            sendMessage(showHelp(), chatId);
        } else if (messageText.equals("/verify")) {
            sendMessage("Please enter your email to verify OTP", chatId);
            userCommands.put(chatId, "IDENTIFIER");
        } else {
            sendMessage("Invalid command. Please type /help to see available commands.", chatId);
        }
    }

    // Xử lý khi người dùng nhập định danh (email/số điện thoại)
    public void requestOtpForIdentifier(String email, String chatId) {
        OTP otp = otpRepository.findByEmail(email);

        if (otp != null) {
            pendingUserVerification.put(chatId, email);
            sendMessage("Please enter OTP sent to your email/phone", chatId);
            userCommands.put(chatId, "VERIFY");
        } else {
            sendMessage("No OTP found for the provided identifier. Please try again.", chatId);
            userCommands.remove(chatId);
        }
    }

    // Xác thực OTP
    public void verifyOtp(int otpInput, String chatId) {
        String identifier = pendingUserVerification.get(chatId); // Lấy định danh của người dùng
        if (identifier != null) {
            OTP otp = otpRepository.findByEmail(identifier);  // Tìm OTP theo định danh

            if (otp != null && otp.getOtpCode() == otpInput) {
                sendMessage("OTP verified successfully!", chatId);
                otp.setIsUsed(true);
                otp.setUsedAt(System.currentTimeMillis());
                userCommands.remove(chatId);  // Xóa trạng thái sau khi xác thực thành công
                pendingUserVerification.remove(chatId);  // Xóa định danh sau khi xác thực

                otpRepository.save(otp);  // Lưu lại chatId sau khi xác thực thành công
            } else {
                sendMessage("Invalid OTP. Please try again.", chatId);
            }
        }
    }

    public static void main(String[] args) {
        NotificationBot bot = new NotificationBot();
        bot.readPropertiesFile();
    }

    public void readPropertiesFile() {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream("config/application.properties");
            prop.load(input);

            TELEGRAM_BOT_USERNAME = prop.getProperty("telegram.bot.username");
            TELEGRAM_BOT_TOKEN = prop.getProperty("telegram.bot.token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
