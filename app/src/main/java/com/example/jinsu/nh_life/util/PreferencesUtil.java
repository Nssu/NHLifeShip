package com.example.jinsu.nh_life.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jinsu.nh_life.common.Constants;
import com.example.jinsu.nh_life.model.User;
import com.google.gson.Gson;

public class PreferencesUtil {
    private static PreferencesUtil instance = null;
    private Gson gson;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;


    private PreferencesUtil()
    {
        gson = new Gson();
    }
    public static PreferencesUtil getInstance()
    {
        if(instance == null)
        {
            instance = new PreferencesUtil();
        }
        return instance;
    }

    private User getPreferences(Context context)
    {
        mPref = context.getSharedPreferences(Constants.getInstance().getPREF_LOCAL_ID(),Context.MODE_PRIVATE);
        String json = mPref.getString(Constants.getInstance().getPREF_USER_KEY(),"");
        return  gson.fromJson(json,User.class);
    }
    private void setPreferences(Context context,User user)
    {
        mPref = context.getSharedPreferences(Constants.getInstance().getPREF_LOCAL_ID(),Context.MODE_PRIVATE);
        editor = mPref.edit();
        String json = gson.toJson(user);
        editor.putString(Constants.getInstance().getPREF_USER_KEY(),json);
        editor.commit();
    }

}
