package com.example.buuktu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static boolean getSharedPreferenceBoolean(String namePreferences,String namePreference, int mode, Context context)
    {
        return context.getSharedPreferences(namePreferences,mode).getBoolean(namePreference,false);
    }
    public static void setSharedPreferenceBoolean(String namePreferences,String namePreference, int mode, Context context,boolean newValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(namePreferences,mode);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(namePreference,newValue);
            editor.commit();
    }
}
