package com.example.buuktu.adapters;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundedBorderSquareTransformation extends BitmapTransformation {

    private final int cornerRadius;
    private final int borderWidth;
    private final int borderColor;

    public RoundedBorderSquareTransformation(int cornerRadius, int borderWidth, int borderColor) {
        this.cornerRadius = cornerRadius;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("rounded_border_square_" + cornerRadius + borderWidth + borderColor).getBytes(CHARSET));
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;

        // Recorta un cuadrado centrado de la imagen original
        Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

        // Redimensiona al tamaño de salida deseado
        Bitmap scaled = Bitmap.createScaledBitmap(squared, outWidth, outHeight, false);

        // Prepara el lienzo de salida
        Bitmap output = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader shader = new BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        float radius = cornerRadius;
        RectF rect = new RectF(borderWidth, borderWidth, outWidth - borderWidth, outHeight - borderWidth);

        // Dibuja la imagen redondeada
        canvas.drawRoundRect(rect, radius, radius, paint);

        // Dibuja el borde exterior
        if (borderWidth > 0) {
            Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        }

        return output;
    }
}
