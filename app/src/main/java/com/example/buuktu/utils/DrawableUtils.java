package com.example.buuktu.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;
import com.example.buuktu.adapters.RoundedBorderSquareTransformation;

import java.io.IOException;

public class DrawableUtils {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius,int borderWidth,@ColorRes int idColor, int mipmap, ImageButton imageButton) {
        int borderColor = ContextCompat.getColor(context, idColor);


        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(mipmap) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageButton);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth, @ColorRes int idColor, Uri uri, ImageView imageView) throws IOException {
        int borderColor = ContextCompat.getColor(context, idColor);

        // 3. Escalar al tamaño deseado (opcional, por ejemplo 150x150
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(uri) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageView);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth, @ColorRes int idColor, Drawable drawable, ImageView imageView) throws IOException {
        int borderColor = ContextCompat.getColor(context, idColor);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(drawable) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageView);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth,@ColorRes int idColor, Uri uri, ImageButton imageButton) {
        int borderColor = ContextCompat.getColor(context, idColor);
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop() // << 👈 evita el recorte
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));
        Glide.with(context)
                .load(uri) // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageButton);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth, @ColorRes int idColor, Bitmap bitmap, ImageButton imageButton) {
        int borderColor = ContextCompat.getColor(context, idColor);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(bitmap)
                .apply(requestOptions)
                .into(imageButton);
    }


    public static void personalizarImagenCircle(Context context, Bitmap bitmap, ImageView imageView,@ColorRes int color) {
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedDrawable.setCircular(true); // Ya hace la imagen circular, no necesita setCornerRadius()
        imageView.setImageDrawable(roundedDrawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(15, 15, 15, 15);


// Obtener el drawable del borde de manera segura
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        if (drawableBorder != null) {
            drawableBorder.setTint(color);
            imageView.setBackground(drawableBorder);
        }

    }
    public static void personalizarImagenCircle(Context context, int mipmapResId, ImageView imageView,@ColorRes int color) {
        // Cargar el drawable desde mipmap
        Drawable drawable = ContextCompat.getDrawable(context, mipmapResId);
        if (drawable == null) {
            Log.e("DrawableUtils", "Drawable no encontrado con ID: " + mipmapResId);
            return;
        }

        // Convertir drawable a bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Crear drawable redondeado
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedDrawable.setCircular(true);

        imageView.setImageDrawable(roundedDrawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(15, 15, 15, 15);

        // Aplicar borde si existe
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        if (drawableBorder != null) {
            drawableBorder.setTint(color);
            imageView.setBackground(drawableBorder);
        }
    }

    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton,@ColorRes int color) {
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedDrawable.setCircular(true); // Ya hace la imagen circular, no necesita setCornerRadius()
        imageButton.setImageDrawable(roundedDrawable);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setPadding(15, 15, 15, 15);


// Obtener el drawable del borde de manera segura
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        if (drawableBorder != null) {
            drawableBorder.setTint(color);
            imageButton.setBackground(drawableBorder);
        }

    }
    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton,@ColorRes int color, boolean pressed) {
        // Crea el drawable para la imagen circular
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedDrawable.setCircular(true);
        imageButton.setImageDrawable(roundedDrawable);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setPadding(15, 15, 15, 15);

        // Obtener el drawable del borde
        GradientDrawable drawableBorder;
        if (pressed) {
            // Crear un nuevo GradientDrawable con el borde más grueso
            drawableBorder = new GradientDrawable();
            drawableBorder.setShape(GradientDrawable.OVAL);
            drawableBorder.setStroke(40, color); // Usar el color proporcionado
        } else {
            // Crear un nuevo GradientDrawable con el borde original
            drawableBorder = new GradientDrawable();
            drawableBorder.setShape(GradientDrawable.OVAL);
            drawableBorder.setStroke(20, color); // Usar el color proporcionado
        }

        imageButton.setBackground(drawableBorder);
    }


}
