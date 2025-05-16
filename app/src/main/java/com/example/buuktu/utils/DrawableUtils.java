package com.example.buuktu.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.buuktu.R;

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

    public static void personalizarImagenCuadradoButton(Context context, int radius, int border, @ColorRes int colorRes, int imageRes, ImageButton button) {
        Glide.with(context)
                .load(imageRes)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, colorRes))))
                .into(button);
    }

    public static void personalizarImagenCuadradoButton(Context context, int radius, int border, @ColorRes int idColor, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, idColor))))
                .into(imageView);

    }
    public static void personalizarImagenCuadradoButton(Context context, int radius, int border, @ColorRes int idColor, Drawable drawable, ImageView imageView) {
        Glide.with(context)
                .load(drawable)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, idColor))))
                .into(imageView);
    }
    public static String getMipmapName(Context context, int id) {
        String fullName = context.getResources().getResourceName(id);
        return fullName.substring(fullName.lastIndexOf("/") + 1);
    }

    public static String getExtensionFromUri(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = cr.getType(uri);
        return "." + mime.getExtensionFromMimeType(type);
    }
    public static void personalizarImagenCuadradoButton(Context context, int radius, int border,@ColorRes int idColor, Uri uri, ImageButton imageButton) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, idColor))))
                .into(imageButton);
    }
    public static void personalizarImagenCuadradoImageView(Context context, int radius, int border,@ColorRes int idColor, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, idColor))))
                .into(imageView);
    }

    public static void personalizarImagenCuadradoButton(Context context, int radius, int border, @ColorRes int idColor, Bitmap bitmap, ImageView imageView) {
        Glide.with(context)
                .load(bitmap)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedBorderSquareTransformation(radius, border, ContextCompat.getColor(context, idColor))))
                .into(imageView);
    }

    public static void personalizarImagenCircle(Context context, Bitmap bitmap, ImageView imageView, @ColorRes int color) {
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageView.setPadding(pad, pad, pad, pad);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 1. Aplicar fondo redondeado con borde
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        drawableBorder.setTint(ContextCompat.getColor(context, color));
        imageView.setBackground(drawableBorder);

        // 2. Usar Glide para aplicar la imagen circular
        Glide.with(context)
                .load(bitmap)
                .circleCrop()
                .into(imageView);

    }
    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton, @ColorRes int color) {
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageButton.setPadding(pad, pad, pad, pad);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 1. Aplicar fondo redondeado con borde
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
            drawableBorder.setTint(ContextCompat.getColor(context, color));
            imageButton.setBackground(drawableBorder);

        // 2. Usar Glide para aplicar la imagen circular
        Glide.with(context)
                .load(bitmap)
                .circleCrop()
                .into(imageButton);
    }


    public static void personalizarImagenCircleButton(Context context, Uri uri, ImageButton imageButton, @ColorRes int color) {
        // 1) Configurar el padding mínimo para el borde
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageButton.setPadding(pad, pad, pad, pad);

        // 2) Fondo oval con trazo (borde)
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        drawableBorder.setTint(ContextCompat.getColor(context, color));

        imageButton.setBackground(drawableBorder);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 3) Glide sólo para recortar la imagen en círculo y volcarla en el ImageButton
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .circleCrop()
                .into(imageButton);
    }
    public static void personalizarImagenCircleButton(Context context, Uri uri, ImageView imageView, @ColorRes int color) {
        // 1) Configurar el padding mínimo para el borde
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageView.setPadding(pad, pad, pad, pad);

        // 2) Fondo oval con trazo (borde)
        Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
        drawableBorder.setTint(ContextCompat.getColor(context, color));

        imageView.setBackground(drawableBorder);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 3) Glide sólo para recortar la imagen en círculo y volcarla en el ImageButton
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .circleCrop()
                .into(imageView);
    }
}
