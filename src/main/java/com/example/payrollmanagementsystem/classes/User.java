package com.example.payrollmanagementsystem.classes;

public class User {
    private String mail;
    private String password;
    private String type;

    public User(String mail, String password, String type) {
        this.mail = mail;
        this.password = password;
        this.type = type;
    }
}
