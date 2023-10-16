package ru.relex.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "dgtu_elders_bot";
    }

    @Override
    public String getBotToken() {
        return "6645254379:AAGJfYfJ_AZmFFHoawmkvZhoqieiZCQnh7I";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        System.out.println(originalMessage.getText());
    }
}
