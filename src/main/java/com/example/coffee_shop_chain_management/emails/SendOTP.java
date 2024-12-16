package com.example.coffee_shop_chain_management.emails;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
public class SendOTP {
    @Value("${gmail.smtp.host}")
    private String SMTP_HOST;
    @Value("${gmail.smtp.port}")
    private String SMTP_PORT;
    @Value("${gmail.smtp.username}")
    private String SMTP_USERNAME;
    @Value("${gmail.smtp.password}")
    private String SMTP_PASSWORD;
    @Value("${email.from}")
    private String EMAIL_FROM;
    @Value("${email.verification.url}")
    private String EMAIL_VERIFICATION_URL;
    @Value("${email.verification.api_key}")
    private String EMAIL_VERIFICATION_API_KEY;

    public int generateOTP() {
        return (int) (Math.random() * 9000) + 1000;
    }

    public boolean isValidEmail(String email) {
        try {
            // Tạo URL đối tượng
            URL url = new URL(EMAIL_VERIFICATION_URL);

            // Mở kết nối với URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức HTTP là POST
            conn.setRequestMethod("POST");

            // Thiết lập headers (nếu cần)
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            // Cho phép ghi dữ liệu vào body của request
            conn.setDoOutput(true);

            // Tạo JSON chứa api_key và email_address
            String apiKey = EMAIL_VERIFICATION_API_KEY; // Thay bằng API key của bạn
            String jsonInputString = String.format("{\"api_key\": \"%s\", \"email_address\": \"%s\"}", apiKey, email);

            // Ghi dữ liệu vào request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Kiểm tra mã phản hồi từ server
            int responseCode = conn.getResponseCode();


            // Đọc phản hồi từ server
            if (responseCode == HttpURLConnection.HTTP_OK) { // nếu server phản hồi thành công
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Kiểm tra email hợp lệ
                    JSONObject data = new JSONObject(response.toString()).getJSONObject("data");

                    boolean format_valid = data.getBoolean("format_valid");
                    boolean mx_found = data.getBoolean("mx_found");

                    return format_valid && mx_found;

                }
            } else {
                System.out.println("POST request failed with response code: " + responseCode);
            }

            // Đóng kết nối
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMail(String text, String sendTo) {
        if (!isValidEmail(sendTo)) {
            System.out.println("Email không hợp lệ: " + sendTo);
            return false;
        }

        // Cấu hình các thuộc tính cho phiên email
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo phiên gửi email với xác thực
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Tạo đối tượng tin nhắn
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
            message.setSubject("Confirm your email");

            // Nội dung HTML với link POST tới API
            String htmlContent = text;

            // Đặt nội dung HTML cho email
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully with HTML content!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void sendEmailWithAttachment(String toEmail, String subject, String body, File file) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Xác thực email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Nội dung email
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(body);

            // File đính kèm
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);

            // Gộp nội dung và file đính kèm
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully with attachment.");
        } catch (Exception e) {
            System.err.println("Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SendOTP sendOTP = new SendOTP();
        sendOTP.sendEmailWithAttachment("nhp1807@gmail.com", "Test email with attachment", "This is a test email with attachment", new File("src/main/resources/application.properties"));
    }
}
