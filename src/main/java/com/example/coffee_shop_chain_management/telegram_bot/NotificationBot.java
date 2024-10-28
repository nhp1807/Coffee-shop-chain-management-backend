package com.example.coffee_shop_chain_management.telegram_bot;

import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Account;
import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.enums.OTPType;
import com.example.coffee_shop_chain_management.repository.AccountRepository;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import com.example.coffee_shop_chain_management.service.AccountService;
import com.example.coffee_shop_chain_management.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class NotificationBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String TELEGRAM_BOT_USERNAME;
    @Value("${telegram.bot.token}")
    private String TELEGRAM_BOT_TOKEN;
    private Map<String, String> userCommands = new HashMap<>();
    private Map<String, String> pendingUserVerification = new HashMap<>();
    @Autowired
    private OTPService otpService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SendOTP sendOTP;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (userCommands.containsKey(chatId) && userCommands.get(chatId).equals("VERIFY")) {
                verifyOtp(Integer.parseInt(messageText), chatId);  // Kiểm tra OTP
            } else if (userCommands.containsKey(chatId) && userCommands.get(chatId).equals("IDENTIFIER")) {
                Account account = accountService.getAccountByEmail(messageText);

                if (account != null && account.getChatID() != null) {
                    sendMessage("This email/phone number is already verified.", chatId);
                    userCommands.remove(chatId);
                    return;
                } else if (account == null) {
                    sendMessage("No account found for the provided identifier. Please try again.", chatId);
                    userCommands.remove(chatId);
                    return;
                }

                int otpCode = sendOTP.generateOTP();
                OTP otp = new OTP(otpCode, messageText, OTPType.REGISTER);
                otpService.createOTP(otp);
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
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Available commands:\n");
        helpMessage.append("/start - Start the bot\n");
        helpMessage.append("/help - Show available commands\n");
        helpMessage.append("/verify - Verify your email\n");

        return helpMessage.toString();
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

    // Kiểm tra lệnh nhập vào
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

    // Xử lý khi người dùng nhập email
    public void requestOtpForIdentifier(String email, String chatId) {
        OTP otp = otpService.getOTPByEmail(email);

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
            Account account = accountService.getAccountByEmail(identifier);
            if (account != null && account.getChatID() != null) {
                sendMessage("This email/phone number is already verified.", chatId);
                userCommands.remove(chatId);
                pendingUserVerification.remove(chatId);
                return;
            } else if (account == null) {
                sendMessage("No account found for the provided identifier. Please try again.", chatId);
                userCommands.remove(chatId);
                pendingUserVerification.remove(chatId);
                return;
            }

            OTP otp = otpService.getOTPByEmail(identifier);

            if (otp != null && otp.getOtpCode() == otpInput) {
                sendMessage("OTP verified successfully!", chatId);
                otp.setIsUsed(true);
                otp.setUsedAt(System.currentTimeMillis());
                userCommands.remove(chatId);
                pendingUserVerification.remove(chatId);

                otpService.updateOTP(otp);

                UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
                updateAccountDTO.setChatID(chatId);

                account.setChatID(chatId);
                accountService.updateAccount(account.getAccountID(), updateAccountDTO);
            } else {
                sendMessage("Invalid OTP. Please try again.", chatId);
            }
        }
    }
}
