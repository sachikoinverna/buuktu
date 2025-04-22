package com.example.buuktu.utils;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;

public class EfectsUtils {
    public static void startCircularReveal(Drawable finalDrawable, ImageButton imageButton) {
        imageButton.setImageDrawable(finalDrawable);
        imageButton.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
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
