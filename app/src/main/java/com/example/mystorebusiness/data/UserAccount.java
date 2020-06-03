package com.example.mystorebusiness.data;

import android.widget.EditText;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private String password, phone, mail, username,name,address,birth,image;
    private int id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static boolean active;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        UserAccount.active = active;
    }

    public UserAccount(EditText username, EditText password, EditText mail)
    {
        this.username = username.getText().toString();
        this.password = password.getText().toString();
        this.mail = mail.getText().toString();
        active=true;
    }

    public UserAccount()
    {
        super();
    }



}
