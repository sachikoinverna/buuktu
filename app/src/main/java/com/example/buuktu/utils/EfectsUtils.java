package com.example.buuktu.utils;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.buuktu.R;

public class EfectsUtils {
    public static void startCircularReveal(Drawable finalDrawable, ImageButton imageButton) {
        imageButton.setImageDrawable(finalDrawable);
        imageButton.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            if (imageButton.isAttachedToWindow()) {

                int centerX = imageButton.getWidth() / 2;
                int centerY = imageButton.getHeight() / 2;

                // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
                float finalRadius = Math.max(imageButton.getWidth(), imageButton.getHeight());

                // Crear el Animator para la revelación circular
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                        imageButton, centerX, centerY, 0, finalRadius);
                circularReveal.setDuration(500); // Duración de la animación en milisegundos

                // Iniciar la animación
                circularReveal.start();
            }
        }
    }
    public static void startCircularReveal(Drawable finalDrawable, ImageView imageView) {
        imageView.setImageDrawable(finalDrawable);
        imageView.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            if (imageView.isAttachedToWindow()) {

                int centerX = imageView.getWidth() / 2;
                int centerY = imageView.getHeight() / 2;

                // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
                float finalRadius = Math.max(imageView.getWidth(), imageView.getHeight());

                // Crear el Animator para la revelación circular
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                        imageView, centerX, centerY, 0, finalRadius);
                circularReveal.setDuration(500); // Duración de la animación en milisegundos

                // Iniciar la animación
                circularReveal.start();
            }
        }
    }
    public static void startCircularReveal(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        imageView.setAlpha(1f);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            int centerX = imageView.getWidth() / 2;
                            int centerY = imageView.getHeight() / 2;
                            float finalRadius = Math.max(imageView.getWidth(), imageView.getHeight());

                            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                                    imageView, centerX, centerY, 0, finalRadius);
                            circularReveal.setDuration(500);
                            circularReveal.start();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // opcional: limpiar si quieres
                    }
                });
    }
    public static void setAnimationsDialog(String phase, LottieAnimationView lottieAnimationView){
        if (lottieAnimationView == null) {
            Log.e("EfectsUtils", "LottieAnimationView es null. No se puede establecer la animación.");
            return;
        }
        switch (phase){

            case "start":
                lottieAnimationView.setAnimation(R.raw.reading_anim);
                break;
            case "success":
                lottieAnimationView.setAnimation(R.raw.success_anim);
                break;
            case "fail":
                lottieAnimationView.setAnimation(R.raw.fail_anim);
                break;
        }
        lottieAnimationView.playAnimation();
    }

}
