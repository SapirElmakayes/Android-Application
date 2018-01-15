package com.example.myapplication;

import android.net.Uri;
import android.support.annotation.Keep;

/**
 * Created by Sapir on 02/12/2017.
 */

public class User {
    String _name, _lastName, _userName, _password, _city, _address, _email;
    String _img;

    public User(){

    }
    public User(String username, String password, String name, String lastName, String city, String address, String email, String img) {
        this._userName = username;
        this._password = password;
        this._name = name;
        this._lastName = lastName;
        this._city = city;
        this._address = address;
        this._email = email;
        this._img = img;
    }
    public String get_password(){
        return _password;
    }
    @Keep
    public boolean isValidPassword(String password){
        if(password.length() >= 6) return true;
        return false;
    }
}
