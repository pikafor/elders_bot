package ru.relex.utils;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {
    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

        return sendMessage;
    }

    public SendMessage generateSendMessageWithCallBackQuery(Update update, String text) {
        var message = update.getCallbackQuery();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getFrom().getId());
        sendMessage.setText(text);

        return sendMessage;
    }
}
