package ru.relex.controller;

import org.example.Connection.SqlConnection;
import org.example.Gui.UsersGui;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardController {

    SqlConnection sqlConnection = new SqlConnection();
        sqlConnection.Connection();

    UsersGui usersGui = new UsersGui(sqlConnection);

        usersGui.mainProcess();
    public SendMessage authorization(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Войти или зарегестрироваться");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Войти");
        inlineKeyboardButton1.setCallbackData("ВОЙТИ");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Регистрация");
        inlineKeyboardButton2.setCallbackData("РЕГИСТРАЦИЯ");
        rowInline1.add(inlineKeyboardButton1);
        rowInline1.add(inlineKeyboardButton2);

        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }

    public SendMessage logIn(long chat_id) {
        SendMessage message = new SendMessage();



        return message;
    }
}
