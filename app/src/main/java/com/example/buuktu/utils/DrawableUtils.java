package com.example.buuktu.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
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
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth, @ColorRes int idColor, Uri uri, ImageView imageView, int mipmapError) throws IOException {
        int borderColor = ContextCompat.getColor(context, idColor);

        // 3. Escalar al tamaño deseado (opcional, por ejemplo 150x150
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(uri).signature(new ObjectKey(System.currentTimeMillis())).error(mipmapError) // Cambia la firma para forzar la recarga
                // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageView);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth, @ColorRes int idColor, Drawable drawable, ImageView imageView) throws IOException {
        int borderColor = ContextCompat.getColor(context, idColor);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));

        Glide.with(context)
                .load(drawable) // Cambia la firma para forzar la recarga
                // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageView);
    }
    public static String getMipmapName(Context context,int id){
        return context.getResources().getResourceName(id);
    }
    public static String getExtensionFromUri(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = cr.getType(uri);
        return "." + mime.getExtensionFromMimeType(type);
    }
    public static void personalizarImagenCuadradoButton(Context context, int cornerRadius, int borderWidth,@ColorRes int idColor, Uri uri, ImageButton imageButton) {
        int borderColor = ContextCompat.getColor(context, idColor);
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop() // << 👈 evita el recorte
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));
        Glide.with(context)
                .load(uri).signature(new ObjectKey(System.currentTimeMillis())) // Cambia la firma para forzar la recarga
                // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageButton);
    }
    public static void personalizarImagenCuadradoImageView(Context context, int cornerRadius, int borderWidth,@ColorRes int idColor, Uri uri, ImageView imageView) {
        int borderColor = ContextCompat.getColor(context, idColor);
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop() // << 👈 evita el recorte
                .transform(new RoundedBorderSquareTransformation(cornerRadius, borderWidth, borderColor));
        Glide.with(context)
                .load(uri).signature(new ObjectKey(System.currentTimeMillis())) // Cambia la firma para forzar la recarga
                // 👍 Esto sí pasa por la transformación
                .apply(requestOptions)
                .into(imageView);
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


    public static void personalizarImagenCircle(Context context, Bitmap bitmap, ImageView imageView, @ColorRes int color) {
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedDrawable.setCircular(true); // Ya hace la imagen circular, no necesita setCornerRadius()
        imageView.setImageDrawable(roundedDrawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2,
                context.getResources().getDisplayMetrics()
        );
        imageView.setPadding(pad, pad, pad, pad);


// Obtener el drawable del borde de manera segura
        Drawable bg = ContextCompat.getDrawable(context, R.drawable.border_register);
        bg.setTint(color);

        imageView.setBackground(bg);

    }
  public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton, @ColorRes int color) {
      // 1) Configurar el padding mínimo para el borde
      int pad = (int) TypedValue.applyDimension(
              TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
      );
      imageButton.setPadding(pad, pad, pad, pad);

      // 2) Fondo oval con trazo (borde)
      Drawable drawableBorder = ContextCompat.getDrawable(context, R.drawable.border_register);
      if (drawableBorder != null) {
          drawableBorder.setTint(color);
          imageButton.setBackground(drawableBorder);
      }
      imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

      // 3) Glide sólo para recortar la imagen en círculo y volcarla en el ImageButton
      Glide.with(context)
              .asBitmap()
              .load(bitmap)
              .override(bitmap.getWidth(), bitmap.getHeight()) // conserva tamaño original
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
        if (drawableBorder != null) {
            drawableBorder.setTint(color);
            imageButton.setBackground(drawableBorder);
        }
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 3) Glide sólo para recortar la imagen en círculo y volcarla en el ImageButton
        Glide.with(context)
                .asBitmap()
                .load(uri)
             //   .override(bitmap.getWidth(), bitmap.getHeight()) // conserva tamaño original
                .circleCrop()
                .into(imageButton);
    }
    public static void personalizarImagenCircleButton(Context context, Bitmap bitmap, ImageButton imageButton, @ColorRes int color, boolean pressed) {
        // 1) Padding mínimo
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageButton.setPadding(pad, pad, pad, pad);

        // 2) Fondo oval con trazo más grueso si está presionado
        GradientDrawable drawableBorder = new GradientDrawable();
        drawableBorder.setShape(GradientDrawable.OVAL);
        int strokeWidthDp = pressed ? 40 : 20;
        int strokePx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, strokeWidthDp, context.getResources().getDisplayMetrics()
        );
        drawableBorder.setStroke(strokePx, color);
        imageButton.setBackground(drawableBorder);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 3) Glide sólo para el circleCrop
        Glide.with(context)
                .asBitmap()
                .load(bitmap)
                .override(bitmap.getWidth(), bitmap.getHeight())
                .circleCrop()
                .into(imageButton);
    }
    public static void personalizarImagenCircleButton(Context context, Uri uri, ImageButton imageButton, @ColorRes int color, boolean pressed) {
        // 1) Padding mínimo
        int pad = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()
        );
        imageButton.setPadding(pad, pad, pad, pad);

        // 2) Fondo oval con trazo más grueso si está presionado
        GradientDrawable drawableBorder = new GradientDrawable();
        drawableBorder.setShape(GradientDrawable.OVAL);
        int strokeWidthDp = pressed ? 40 : 20;
        int strokePx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, strokeWidthDp, context.getResources().getDisplayMetrics()
        );
        drawableBorder.setStroke(strokePx, color);
        imageButton.setBackground(drawableBorder);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 3) Glide sólo para el circleCrop
        Glide.with(context)
                .asBitmap()
                .load(uri)
            //    .override(bitmap.getWidth(), bitmap.getHeight())
                .circleCrop()
                .into(imageButton);
    }
}
