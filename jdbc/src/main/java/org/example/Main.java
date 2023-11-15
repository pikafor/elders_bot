package org.example;

import org.example.Connection.SqlConnection;
import org.example.Gui.UsersGui;

public class Main {
    public static void main(String[] args) {
        SqlConnection sqlConnection = new SqlConnection();
        sqlConnection.Connection();

        UsersGui usersGui = new UsersGui(sqlConnection);

        usersGui.mainProcess(5);
    }
}