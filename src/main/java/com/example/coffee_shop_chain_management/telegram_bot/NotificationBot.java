package com.example.coffee_shop_chain_management.telegram_bot;

import com.example.coffee_shop_chain_management.dto.UpdateAccountDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.enums.OTPType;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.AccountResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import com.example.coffee_shop_chain_management.service.AccountService;
import com.example.coffee_shop_chain_management.service.EmployeeService;
import com.example.coffee_shop_chain_management.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    private EmployeeService employeeService;

    @Autowired
    private SendOTP sendOTP;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (userCommands.containsKey(chatId)) {
                switch (userCommands.get(chatId)) {
                    case "VERIFY":
                        verifyOtp(Integer.parseInt(messageText), chatId);
                        break;
                    case "IDENTIFIER":
                        processIdentifier(messageText, chatId);
                        break;
                    default:
                        sendMessage("Unknown command. Please try again.", chatId);
                }
            } else {
                checkInput(messageText, chatId);
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
        return """
                Available commands:
                /start - Start the bot
                /help - Show available commands
                /verify - Verify your email
                """;
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
        switch (messageText) {
            case "/start" -> {
                sendMessage("Welcome to Coffee Shop Chain Management Bot!", chatId);
                sendMessage("Please type /help to see available commands.", chatId);
            }
            case "/help" -> sendMessage(showHelp(), chatId);
            case "/verify" -> {
                sendMessage("Please enter your email to verify OTP", chatId);
                userCommands.put(chatId, "IDENTIFIER");
            }
            default -> sendMessage("Invalid command. Please type /help to see available commands.", chatId);
        }
    }

    public void processIdentifier(String identifier, String chatId) {
        APIResponse<AccountResponse> accountResponse = accountService.getAccountByEmail(identifier);
        AccountResponse account = accountResponse.getData();

        APIResponse<EmployeeResponse> employeeResponse = employeeService.getEmployeeByEmail(identifier);
        EmployeeResponse employee = employeeResponse.getData();

        if (account != null && account.getChatID() != null) {
            sendMessage("This email/phone number is already verified in account.", chatId);
            cleanupVerificationState(chatId);
            return;
        }

        if (employee != null && employee.getChatID() != null) {
            sendMessage("This email/phone number is already verified in employee.", chatId);
            cleanupVerificationState(chatId);
            return;
        }

        if (account == null && employee == null) {
            sendMessage("No account or employee found for the provided identifier. Please try again.", chatId);
            cleanupVerificationState(chatId);
            return;
        }

        int otpCode = sendOTP.generateOTP();
        OTP otp = new OTP(otpCode, identifier, OTPType.REGISTER);
        otpService.createOTP(otp);
        sendOTP.sendMail("Your OTP is: " + otpCode, identifier);
        requestOtpForIdentifier(identifier, chatId);
    }

    public void requestOtpForIdentifier(String email, String chatId) {
        OTP otp = otpService.getOTPByEmail(email);

        if (otp != null) {
            pendingUserVerification.put(chatId, email);
            sendMessage("Please enter OTP sent to your email/phone", chatId);
            userCommands.put(chatId, "VERIFY");
        } else {
            sendMessage("No OTP found for the provided identifier. Please try again.", chatId);
            cleanupVerificationState(chatId);
        }
    }

    public void verifyOtp(int otpInput, String chatId) {
        String identifier = pendingUserVerification.get(chatId);

        if (identifier == null) {
            sendMessage("No pending verification found. Please start the process again.", chatId);
            return;
        }

        APIResponse<AccountResponse> accountResponse = accountService.getAccountByEmail(identifier);
        AccountResponse account = accountResponse.getData();

        APIResponse<EmployeeResponse> employeeResponse = employeeService.getEmployeeByEmail(identifier);
        EmployeeResponse employee = employeeResponse.getData();

        OTP otp = otpService.getOTPByEmail(identifier);

        if (otp != null && otp.getOtpCode() == otpInput && LocalDateTime.now().isBefore(otp.getExpiredAt())) {
            sendMessage("OTP verified successfully!", chatId);
            otp.setIsUsed(true);
            otp.setUsedAt(LocalDateTime.now());
            otpService.updateOTP(otp);

            if (account != null) {
                UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
                updateAccountDTO.setChatID(chatId);
                accountService.updateAccount(account.getAccountID(), updateAccountDTO);
            } else if (employee != null) {
                employeeService.updateEmployeeChatId(employee.getEmployeeID(), chatId);
            }

            cleanupVerificationState(chatId);
        } else {
            sendMessage("Invalid OTP. Please try again.", chatId);
        }
    }

    public void cleanupVerificationState(String chatId) {
        userCommands.remove(chatId);
        pendingUserVerification.remove(chatId);
    }
}