package com.example.buuktu.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.Image;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

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
    public static void personalizarImagenCuadrado(Context context, Bitmap bitmap, ImageView imageView, int color) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int x = (bitmap.getWidth() - size) / 2;
        int y = (bitmap.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);

        float cornerRadius = size / 6f;
        int borderWidth = 12; // Grosor del borde (ajusta según necesites)

        // Crear un Bitmap mutable para dibujar la imagen con el borde
        Bitmap borderedBitmap = Bitmap.createBitmap(squaredBitmap.getWidth() + borderWidth * 2,
                squaredBitmap.getHeight() + borderWidth * 2, squaredBitmap.getConfig());
        Canvas canvas = new Canvas(borderedBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Dibujar el fondo redondeado (opcional, si quieres un fondo del color del borde)
        // paint.setColor(color);
        // paint.setStyle(Paint.Style.FILL);
        // canvas.drawRoundRect(new RectF(0, 0, borderedBitmap.getWidth(), borderedBitmap.getHeight()), cornerRadius + borderWidth / 2f, cornerRadius + borderWidth / 2f, paint);

        // Dibujar la imagen redondeada centrada
        Path path = new Path();
        path.addRoundRect(new RectF(borderWidth, borderWidth, borderedBitmap.getWidth() - borderWidth, borderedBitmap.getHeight() - borderWidth), cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(squaredBitmap, borderWidth, borderWidth, null);
        canvas.clipPath(new Path()); // Limpiar el clip

        // Dibujar el borde redondeado
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(new RectF(borderWidth, borderWidth, borderedBitmap.getWidth() - borderWidth, borderedBitmap.getHeight() - borderWidth), cornerRadius, cornerRadius, paint);

        imageView.setImageBitmap(borderedBitmap);

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int margin = 15;
        if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).setMargins(margin, margin, margin, margin);
        } else if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).setMargins(margin, margin, margin, margin);
        } else if (params instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) params).setMargins(margin, margin, margin, margin);
        }
        imageView.setLayoutParams(params);
    }

    public static void personalizarImagenCuadradoButton(Context context, Bitmap bitmap, ImageButton imageButton, int color) {
        // Asegurar que la imagen sea cuadrada y centrada
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int x = (bitmap.getWidth() - size) / 2;
        int y = (bitmap.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);

        // Crear drawable con bordes redondeados para la imagen
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), squaredBitmap);
        float cornerRadius = size / 6f; // Bordes redondeados (ajustable)
        roundedDrawable.setCornerRadius(cornerRadius);

        // Crear un borde redondeado con ShapeDrawable
        ShapeDrawable borderDrawable = new ShapeDrawable(new RoundRectShape(
                new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius, cornerRadius, cornerRadius}, // Radios de los bordes
                null, null
        ));
        borderDrawable.getPaint().setColor(color); // Color del borde
        borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
        borderDrawable.getPaint().setStrokeWidth(12); // 🔥 Grosor del borde aumentado

        // Usar LayerDrawable para combinar la imagen con el borde
        Drawable[] layers = new Drawable[2];
        layers[0] = roundedDrawable; // Imagen redondeada
        layers[1] = borderDrawable;  // Borde redondeado

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        // Ajustar LayoutParams correctamente según el ViewGroup padre
        ViewGroup.LayoutParams params = imageButton.getLayoutParams();
        if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).setMargins(15, 15, 15, 15); // Márgenes más amplios
        } else if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).setMargins(15, 15, 15, 15);
        } else if (params instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) params).setMargins(15, 15, 15, 15);
        }

        imageButton.setLayoutParams(params);
        imageButton.setImageDrawable(layerDrawable); // Aplicar la imagen con el borde como una capa
    }



    public static void personalizarImagenCircle(Context context, Bitmap bitmap, ImageView imageView, int color) {
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
    public static void personalizarImagenCircle(Context context, int mipmapResId, ImageView imageView, int color) {
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

    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton, int color) {
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
    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton, int color, boolean pressed) {
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
