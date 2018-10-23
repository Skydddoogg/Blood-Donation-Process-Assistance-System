package com.nawinc27.mac.blooddonorfrontend.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "blooddonorrecorder";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public boolean checkLogin(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUsername(){
        return pref.getString(KEY_NAME, null);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}