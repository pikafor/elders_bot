package org.example;

import org.example.Connection.SqlConnection;
import org.example.Gui.UsersGui;

public class Main {
    public static void main(String[] args) {
        SqlConnection sqlConnection = new SqlConnection();
        sqlConnection.Connection();

        UsersGui usersGui = new UsersGui(sqlConnection);

        usersGui.mainProcess();

//        String arr = "Биба,Боба";
//        String arr2 = "";

//        for (int i = 0; i < arr.length(); i++) {
//            if (arr.charAt(i) >= 'А' && arr.charAt(i) <= 'я') {
//                arr2 += arr.charAt(i);
//            } else {
//                arr2 += ' ';
//            }
//        }
//        System.out.println(arr2);
    }
}