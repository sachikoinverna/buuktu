package com.example.buuktu.adapters;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundedBorderTransformation extends BitmapTransformation {

    private final float radius;
    private final float borderWidth;
    private final int borderColor;

    public RoundedBorderTransformation(float radius, float borderWidth, int borderColor) {
        this.radius = radius;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    @NonNull
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        // Dibujar la imagen redondeada
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
        canvas.drawPath(path, paint);

        // Dibujar el borde
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStrokeJoin(Paint.Join.ROUND);
      //  canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, borderPaint);
        canvas.drawRoundRect(
                new RectF(borderWidth / 2f, borderWidth / 2f, width - borderWidth / 2f, height - borderWidth / 2f),
                radius,
                radius,
                borderPaint
        );
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("rounded_border_" + radius + "_" + borderWidth + "_" + borderColor).getBytes());
    }
}
