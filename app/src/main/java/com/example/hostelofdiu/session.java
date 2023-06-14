package com.example.hostelofdiu;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN="IsLoggedIn";
    public static final String KEY_NAME="username";
    public static final String KEY_PHONENUMBER="userphone";
    public static final String KEY_TYPE="type";

    public session(Context _context){
        context=_context;
        preferences=context.getSharedPreferences("userLoginSession",context.MODE_PRIVATE);
        editor=preferences.edit();
    }
    public void createLoginSession(String username,String userphone,String type){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NAME,username);
        editor.putString(KEY_PHONENUMBER,userphone);
        editor.putString(KEY_TYPE,type);
        editor.commit();
    }
    public HashMap<String,String> getuserdetailFromSession(){
        HashMap<String,String> userData=new HashMap<String,String>();
        userData.put(KEY_NAME,preferences.getString(KEY_NAME,null));
        userData.put(KEY_PHONENUMBER,preferences.getString(KEY_PHONENUMBER,null));
        userData.put(KEY_TYPE,preferences.getString(KEY_TYPE,null));
         return userData;
    }
    public boolean checkLogin(){
        if(preferences.getBoolean(IS_LOGIN,false)){
            return true;
        }else {
            return false;
        }
    }
    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }
}
