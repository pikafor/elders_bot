package ru.relex.controller;

import org.example.Connection.SqlConnection;
import org.example.Gui.UsersGui;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class KeyboardController {
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

    public SendMessage mainMenu(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Главное меню");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Добавить пару");
        inlineKeyboardButton1.setCallbackData("ДОБАВИТЬ_ПАРУ");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Добавить студента");
        inlineKeyboardButton2.setCallbackData("ДОБАВИТЬ_СТУДЕНТА");
        rowInline1.add(inlineKeyboardButton1);
        rowInline1.add(inlineKeyboardButton2);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("Отметить");
        inlineKeyboardButton3.setCallbackData("ОТМЕТИТЬ");
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText("excel");
        inlineKeyboardButton4.setCallbackData("EXCEL");
        rowInline2.add(inlineKeyboardButton3);
        rowInline2.add(inlineKeyboardButton4);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        inlineKeyboardButton5.setText("Выход");
        inlineKeyboardButton5.setCallbackData("ВЫХОД");
        rowInline3.add(inlineKeyboardButton5);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }

    public void logIn() {
        SqlConnection sqlConnection = new SqlConnection();
        sqlConnection.Connection();
        UsersGui usersGui = new UsersGui(sqlConnection);
        sqlConnection.Close();
        
        usersGui.getExelTable();
    }
}
