package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_DATA = "pref_data";

    private static final String NAME ="name";
    private static final String EMAIL = "email";
    private static final String AGE = "age";
    private static final String PHOME = "phone";
    private static final String ISLOVE = "islove";

    private final SharedPreferences preferences;

    UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
    }

    public void setUser(UserModel value){
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(NAME, value.getName());
        editor.putString(EMAIL, value.getEmail());
        editor.putInt(AGE, value.getAge());
        editor.putString(PHOME, value.getPhoneNumber());
        editor.putBoolean(ISLOVE, value.isLove());
    }

    UserModel getUser(){
        UserModel model = new UserModel();
        model.setName(preferences.getString(NAME, ""));
        model.setEmail(preferences.getString(EMAIL, ""));
        model.setAge(preferences.getInt(AGE, 0));
        model.setPhoneNumber(preferences.getString(PHOME, ""));
        model.setLove(preferences.getBoolean(ISLOVE, false));

        return model;
    }
}