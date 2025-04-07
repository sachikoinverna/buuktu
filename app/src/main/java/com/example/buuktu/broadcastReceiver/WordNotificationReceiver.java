package com.example.buuktu.broadcastReceiver;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.buuktu.DataBinderMapperImpl;
import com.example.buuktu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WordNotificationReceiver extends BroadcastReceiver {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí deberías obtener la palabra del día desde almacenamiento o API
        String phrase = "Palabra del dia disponible.";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "word_day_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sonido = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.word_of_the_day_notikie);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelId, "Palabra del Día", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(sonido, audioAttributes); // ✅ Aquí fijas tu sonido personalizado
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("buuk-tu").setContentText(phrase)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(R.mipmap.boo_vector).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.words_of_the_day_notikie));

        notificationManager.notify(1, builder.build());
        Map<String,Object> notikieData = new HashMap<>();
        notikieData.put("message",phrase);
        notikieData.put("icon",R.drawable.twotone_translate_24);
        notikieData.put("date", FieldValue.serverTimestamp());
        db.collection("Notikies").add(notikieData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Log.d("NotiTest", "Notificación enviada");

    }

}
