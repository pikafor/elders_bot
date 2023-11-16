package org.example.Gui;

import org.example.Connection.SqlConnection;
import org.example.Controller.ExelController;
import org.example.Controller.SqlController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UsersGui {
    private boolean isRequest = true;
    SqlController sqlController;

    public UsersGui(SqlConnection sqlConnection) {
        sqlController = new SqlController(sqlConnection.getConnection());
    }

    public void mainProcess(int choice) {
        //sqlController.addStudent();

//            System.out.println("1) Добавить пару");
//            System.out.println("2) Показать присутсвующих");
//            System.out.println("3) Добавить студента");
//            System.out.println("4) Отметить");
//            System.out.println("5) конвертировать в exel");
//            System.out.print("Выберите действие: ");

            Scanner scanner = new Scanner(System.in);

//            try {
//                choice = scanner.nextInt();
//            } catch (Exception e) {
//                System.out.println("Вы ввели не число");
//            }

            switch (choice) {
                case 1: {
                    System.out.println("Введите дату");
                    String tableName = scanner.next();
                    sqlController.addColumn('"' + tableName + '"');
                    String pairsDate = scanner.next();
                    sqlController.setPairs(tableName, pairsDate);
                    break;
                }
                case 2: {
                    sqlController.showTable();
                    break;
                }
                case 3: {
                    System.out.print("Введите фамилию: ");
                    String lastName = scanner.next();
                    System.out.print("Введите имя: ");
                    String firstName = scanner.next();
                    sqlController.addStudent(lastName, firstName);
                    break;
                }
                case 4: {
                    System.out.println("Введите Фамилию");
                    String lastName = scanner.next();
                    System.out.println("Введите дату");
                    String date = '"' + scanner.next() + '"';
                    sqlController.mention(lastName, date);
                    break;
                }
                case 5: {
                    ExelController exelController = new ExelController();
                    //return exelController.write(" ", sqlController.getResultSetMetaData("test"), sqlController.getResult("test"), sqlController.getResultSetMetaData("pairs"), sqlController.getResult("pairs"), sqlController.lastId("pairs"));
                    break;
                }
                default: {
                    System.out.println("Нет такого действия.");
                    break;
                }
            }
        }

        public void getExelTable() {
            ExelController exelController = new ExelController();
            exelController.write(" ", sqlController.getResultSetMetaData("test"), sqlController.getResult("test"), sqlController.getResultSetMetaData("pairs"), sqlController.getResult("pairs"), sqlController.lastId("pairs"));
        }
        public String getPath() {
            ExelController exelController = new ExelController();
            return exelController.getPath();
        }
        public void setNewStudent(String text) {
            List<String> splitArray = split(text);
            sqlController.addStudent(splitArray.get(0), splitArray.get(1));
        }

        private List<String> split(String text) {
            List<String> splitArray = new ArrayList<>();
            String interim = "";
            int _i = 0;

            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == ' ') {
                    splitArray.add(interim);
                    _i = i;
                    break;
                }
                interim += text.charAt(i);
            }
            interim = "";
            for (int i = _i+1; i < text.length(); i++) {
                interim += text.charAt(i);
            }
            splitArray.add(interim);
            return splitArray;
        }

        public void mention(String text) {
            List<String> splitArray = split(text);
            sqlController.mention(splitArray.get(0), splitArray.get(1));
        }
}
