package com.example.finalproject.Model;

public class User {

    private String name;
    private String Password;
    private String phone;




    public User(String name, String password) {
        this.name = name;
        Password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
