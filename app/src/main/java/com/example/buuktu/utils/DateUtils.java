package com.example.buuktu.utils;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }
    public static boolean hasDateChanged(String date){
        return !getCurrentDate().equals(date);
    }

    public static long  currentMs(){
        return System.currentTimeMillis();
    }
    public static long getTimeDiference(long date1,long date2){
        return date1-date2;
    }
    public static long secondsFrom(long date){
        return TimeUnit.MILLISECONDS.toSeconds(date);
    }
    public static long minutesFrom(long date){
        return TimeUnit.MILLISECONDS.toMinutes(date);
    }
    public static long hoursFrom(long date){
        return TimeUnit.MILLISECONDS.toHours(date);
    }
    public static long daysFrom(long date){
        return TimeUnit.MILLISECONDS.toDays(date);
    }

}
