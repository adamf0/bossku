package app.adam.bossku.helper;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import app.adam.bossku.view.ui.LoginActivity;

public class SessionManager {
    public SharedPreferences pref;
    public Editor editor;
    public Context _context;

    public static final String PREF_NAME = "Session";
    public static final String IS_LOGIN = "islogin";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_ROLE = "role";
    public static final String KEY_EMAIL_STATUS = "email_status";
    public static final String KEY_HAS_VERIFIED = "has_verified";
    public static final String KEY_NAME = "name";
    public static final String KEY_NEED_CORRECTION = "need_correction";
    public static final String KEY_NOTIFICATION = "notification";
    public static final String KEY_PHOTO_PROFILE = "pp";
    public static final String KEY_ROLE_NAME = "role_name";

    public void createSessionLogin(String token, String role){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getSessionLogin(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _context.startActivity(i);
    }
    private boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}