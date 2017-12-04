package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by Sapir on 02/12/2017.
 */

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDB;

    public UserLocalStore(Context context) {
        userLocalDB = context.getSharedPreferences(SP_NAME, 0);
    }


    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putString("userName", user._userName);
        spEditor.putString("password", user._password);
        spEditor.putString("name", user._name);
        spEditor.putString("lastName", user._lastName);
        spEditor.putString("city", user._city);
        spEditor.putString("address", user._address);
        spEditor.putString("email", user._email);
        spEditor.putString("img", user._img);

        spEditor.commit();
    }

    public User getLoggedUser() {
        String userName = userLocalDB.getString("userName", "");
        String password = userLocalDB.getString("password", "");
        String name = userLocalDB.getString("name", "");
        String lastName = userLocalDB.getString("lastName", "");
        String city = userLocalDB.getString("city", "");
        String address = userLocalDB.getString("address", "");
        String email = userLocalDB.getString("email", "");
        String img = userLocalDB.getString("img", "");

        User storedUser = new User(userName, password, name, lastName, city, address, email, img);

        return storedUser;
    }

    public void setUserLoggedIn(boolean LoggedIn) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putBoolean("loggedIn", LoggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if(userLocalDB.getBoolean("loggedIn", false)) {
            return true;
        }
        return false;
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
