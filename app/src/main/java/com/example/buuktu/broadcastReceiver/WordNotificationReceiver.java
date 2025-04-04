package com.example.buuktu.broadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.buuktu.R;

public class WordNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí deberías obtener la palabra del día desde almacenamiento o API
        String phrase = "Palabra del dia ya disponible.";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "word_day_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Palabra del Día", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(phrase))
                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true).setColor(Color.RED);
                //.setSound();
       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("📚 Buktu")
                .setContentText(phrase)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);*/
        notificationManager.notify(1, builder.build());
        Log.d("NotiTest", "Notificación enviada");

    }

}
