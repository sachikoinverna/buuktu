package com.example.buuktu.broadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.buuktu.R;
import com.example.buuktu.models.NotikieModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;

public class WordNotificationReceiver extends BroadcastReceiver {
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    NotificationChannel channel;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí deberías obtener la palabra del día desde almacenamiento o API
        String phrase = context.getString(R.string.word_of_the_day_available);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "word_day_channel";

             channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                Uri sonido = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.word_of_the_day_notikie);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();

                channel = new NotificationChannel(channelId, "Palabra del Día", NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(sonido, audioAttributes);
                notificationManager.createNotificationChannel(channel);
            }
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) return; // Evita enviar la notificación si está bloqueado


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("buuk-tu").setContentText(phrase)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(R.mipmap.boo_vector).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.words_of_the_day_notikie));

        notificationManager.notify(1, builder.build());
        db.collection("Notikies").add(new NotikieModel(phrase,new Timestamp(Instant.now()),R.drawable.twotone_translate_24, FirebaseAuth.getInstance().getUid())).addOnSuccessListener(documentReference -> {

        });

    }

}
