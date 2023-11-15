package ru.relex;

public class User {
    private String logIn;
    private String password;

    public String getLogIn() {
        return logIn;
    }

    public void setLogIn(String logIn) {
        this.logIn = logIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuth() {
        if (logIn != null && password != null)
            return true;
        return false;
    }
}
