import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;

public class EmailSender {
    public static void main(String[] args) {

        String host = "smtp.gmail.com";
        String port = "587";
        String username = "kora.watercolor@gmail.com"; // пошта
        String password = "qazwsx123D"; // пароль до пошти

        List<String> recipients = Arrays.asList("niki097875@gmail.com");

        String subject = "HTML Email with Attachments";
        String htmlMessage = "<h1>Вітаю!</h1><p>Перевірка.</p>";

        List<File> attachments = Arrays.asList(
                new File("D:/Осінній семестр 2024/Network Programming/Creation e-mail sender with html content and attachments/foto1.pdf"),
                new File("D:/Осінній семестр 2024/Network Programming/Creation e-mail sender with html content and attachments/foto2.jpg")
        );

        sendEmail(host, port, username, password, recipients, subject, htmlMessage, attachments);
    }

    public static void sendEmail(String host, String port, String username, String password,
                                 List<String> recipients, String subject, String htmlMessage,
                                 List<File> attachments) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            for (String recipient : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }

            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlMessage, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (File file : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Лист успішно відправлений до: " + recipients);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}