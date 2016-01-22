package ru.icl.parser.sendmail;

public interface MessageSender {
    void sendMessage(String subject, String text, String toEmail);
}
