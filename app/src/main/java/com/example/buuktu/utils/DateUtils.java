package com.example.buuktu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }
    public static boolean hasDateChanged(String date){
        return !getCurrentDate().equals(date);
    }
}
