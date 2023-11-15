package ru.relex.controller;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.relex.User;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    private User user = new User();

    @Value("${bot.token}")
    private String botToken;
    private UpdateController updateController;
    private boolean isWait = false;
    private String lastMessage;


    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    public User getUser() {
        return user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean getIsWait() {
        return isWait;
    }

    public void setWait(boolean wait) {
        isWait = wait;
    }

    public void setUserLogin(Update update) {
        user.setLogIn(update.getMessage().getText());
    }
    public void setUserPassword(Update update) {
        user.setPassword(update.getMessage().getText());
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(user.getLogIn() + " " + user.getPassword() + " " + isWait);
        updateController.processUpdate(update);
        System.out.println(user.getLogIn() + " " + user.getPassword() + " " + isWait);
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    public void sendAnswerDocument(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
