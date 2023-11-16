package org.example.Controller;

import java.sql.*;

public class SqlController {
    private Connection connection;
    public SqlController(Connection connection) {
        this.connection = connection;
    }

    private void setString(PreparedStatement preparedStatement, int id, String value) {
        try {
            preparedStatement.setString(id, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setInt(PreparedStatement preparedStatement, int id, int value) {
        try {
            preparedStatement.setInt(id, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createTable() {
        String tableName = " test ";
        String sql = "create table " + tableName + " (id integer PRIMARY KEY, initials varchar(40));";
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addColumn(String tableName) {
        String sql = "ALTER TABLE test ADD COLUMN " + tableName + " varchar(15) default (' ');";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void mention(String lastName, String date) {
        String pairDate = '"' + date + '"';
        String sql = "update test set " + pairDate + " = 'Н' where last_name = '" + lastName + "'";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTable() {
        String sql = "select * from test ORDER BY id ASC";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            //int count =

            System.out.print("id ");
            System.out.print("Фамилия ");
            System.out.print("Имя ");
            System.out.println(resultSetMetaData.getColumnName(4));
            while(resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(show(i, resultSetMetaData)) + " ");
                    if (i == columnCount) {
                    }
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addStudent(String lastName, String firstName) {
        String sql = "insert into test (id, last_name, first_name) values (?, ?, ?)";
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(sql);
            setInt(preparedStatement, 1, lastId("test"));
            setString(preparedStatement, 2, lastName);
            setString(preparedStatement, 3, firstName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String show(int i, ResultSetMetaData resultSetMetaData) {
        try {
            return resultSetMetaData.getColumnName(i);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getResult(String db) {
        try {
            return connection.createStatement().executeQuery("select * from " + db + " ORDER BY id ASC");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSetMetaData getResultSetMetaData(String db) {
        try {
            return connection.createStatement().executeQuery("select * from " + db +" ORDER BY id ASC").getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPairs(String date, String type) {
        int maxId = lastId("pairs");
        String sql = "insert into pairs (id, pairs_date, pairs_type) values (?, ?, ?);";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setInt(preparedStatement, 1, maxId);
            setString(preparedStatement, 2, date);
            setString(preparedStatement, 3, type);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int lastId(String tableName) {
        int idCount = 1;
        try {
            String sql = "select * from " + tableName + " ORDER BY id ASC";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()){
                idCount++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idCount;
    }
}
