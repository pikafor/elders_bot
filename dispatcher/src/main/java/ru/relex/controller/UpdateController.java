package ru.relex.controller;

import lombok.extern.log4j.Log4j;
import org.example.Connection.SqlConnection;
import org.example.Controller.SqlController;
import org.example.Gui.UsersGui;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.relex.User;
import ru.relex.service.UpdateProducer;
import ru.relex.utils.MessageUtils;

import java.io.FileOutputStream;

import static ru.relex.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public  void processUpdate(Update update) {

        if (!telegramBot.getIsWait()) {
            if (telegramBot.getUser().isAuth()) {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    long chat_id = update.getMessage().getChatId();
                    if (update == null) {
                        log.error("Received update is null");
                        return;
                    }
                    if (update.getMessage() != null) {
                        distributeMessagesByType(update);
                    } else {
                        log.error("Received unsupported message type is Received: " + update);
                    }
                }
                if (update.hasCallbackQuery()) {
                    String call_data = update.getCallbackQuery().getData();
                    if (call_data.equals("EXCEL")) {
                        SqlConnection sqlConnection = new SqlConnection();
                        sqlConnection.Connection();
                        UsersGui usersGui = new UsersGui(sqlConnection);
                        SendDocument sendDocument = new SendDocument();
                        sendDocument.setChatId(update.getCallbackQuery().getFrom().getId());
                        usersGui.getExelTable();
                        String path = "C:\\Users\\Иван\\Desktop\\write.xlsx";
                        InputFile inputFile = new InputFile(new java.io.File(path));
                        sendDocument.setDocument(inputFile);
                        telegramBot.sendAnswerDocument(sendDocument);
                    } else if (call_data.equals("ДОБАВИТЬ_СТУДЕНТА")) {
                        telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithCallBackQuery(update, "Введите фио"));
                        setExpectations("ДОБАВИТЬ_СТУДЕНТА", true);
                    } else if (call_data.equals("ОТМЕТИТЬ")) {
                        telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithCallBackQuery(update, "Введите фамилию и дату через пробел"));
                        setExpectations("ОТМЕТИТЬ", true);
                    }
                }
            } else {
                requestLogin(update);
            }
        } else {
            updateMessagesWithAPause(update);
        }
    }

    private boolean checkLog() {
        if (telegramBot.getUser().getLogIn() != null)
            return true;
        return false;
    }
    private boolean checkPass() {
        if (telegramBot.getUser().getPassword() != null)
            return true;
        return false;
    }
    private void logIn(Update update) {
        long chat_id = update.getCallbackQuery().getMessage().getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat_id);
        sendMessage.setText("Введите логин");
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if(message.getDocument() != null) {
            processDocumentMessage(update);
        } else if(message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Неподдерживаемый тип сообщения!");
        InputFile inputFile = new InputFile("C:\\Users\\Иван\\Desktop\\a.zip");
        setView(update.getCallbackQuery().getMessage().getChatId(), "", inputFile);
    }

    private void setFileIsReceivesView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Файл получен, обрабатывается...");
        setView(sendMessage);
    }

    private void setView(long chat_id, String caption, InputFile sendFile) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chat_id);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(sendFile);
        telegramBot.sendAnswerDocument(sendDocument);
    }
    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivesView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void setExpectations(String message, boolean isWait) {
        telegramBot.setLastMessage(message);
        telegramBot.setWait(isWait);
    }

    private void requestLogin(Update update) {
        KeyboardController keyboardController = new KeyboardController();
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            try {
                telegramBot.execute(keyboardController.authorization(chat_id));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } if (update.hasCallbackQuery()) {

            String call_data = update.getCallbackQuery().getData();

            if (call_data.equals("РЕГИСТРАЦИЯ")) {
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithCallBackQuery(update, "Введите логин"));
                setExpectations("РЕГИСТРАЦИЯ_ЛОГИН", true);
            } else {
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithCallBackQuery(update, "Введите логин"));
                setExpectations("ВХОД_ЛОГИН", true);
            }
        }
    }

    private void updateMessagesWithAPause(Update update) {
        KeyboardController keyboardController = new KeyboardController();
        if (update.getMessage().hasText()) {
            if (telegramBot.getLastMessage().equals("РЕГИСТРАЦИЯ_ЛОГИН")) {
                telegramBot.getUser().setLogIn(update.getMessage().getText());
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithText(update, "Введите пароль"));
                setExpectations("РЕГИСТРАЦИЯ_ПАРОЛЬ", true);
            } else if (telegramBot.getLastMessage().equals("РЕГИСТРАЦИЯ_ПАРОЛЬ")) {
                telegramBot.getUser().setPassword(update.getMessage().getText());
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithText(update, "Вы зарегестрированы"));
                setExpectations("ГЛАВНОЕ_МЕНЮ", false);
                try {
                    telegramBot.execute(keyboardController.mainMenu(update.getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (telegramBot.getLastMessage().equals("ВХОД_ЛОГИН")) {
                telegramBot.getUser().setLogIn(update.getMessage().getText());
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithText(update, "Введите пароль"));
                setExpectations("ВХОД_ПАРОЛЬ", true);
            } else if (telegramBot.getLastMessage().equals("ВХОД_ПАРОЛЬ")) {
                telegramBot.getUser().setPassword(update.getMessage().getText());
                telegramBot.sendAnswerMessage(messageUtils.generateSendMessageWithText(update, "Вы вошли"));
                setExpectations("Вы вошли", false);
                try {
                    telegramBot.execute(keyboardController.mainMenu(update.getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (telegramBot.getLastMessage().equals("ДОБАВИТЬ_СТУДЕНТА")) {
                SqlConnection sqlConnection = new SqlConnection();
                sqlConnection.Connection();
                UsersGui usersGui = new UsersGui(sqlConnection);
                usersGui.setNewStudent(update.getMessage().getText());
                telegramBot.setWait(false);
            } else if (telegramBot.getLastMessage().equals("ОТМЕТИТЬ")) {
                SqlConnection sqlConnection = new SqlConnection();
                sqlConnection.Connection();
                UsersGui usersGui = new UsersGui(sqlConnection);
                usersGui.mention(update.getMessage().getText());
                telegramBot.setWait(false);
            }
        }
    }
}
