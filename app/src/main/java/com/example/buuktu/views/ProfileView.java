package com.example.buuktu.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.buuktu.R;
import com.example.buuktu.utils.BitmapUtils;

public class ProfileView extends AppCompatActivity {
 private ImageButton ib_profileView;
 private TextView tv_usernameProfileView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ib_profileView = findViewById(R.id.ib_profileView);
        tv_usernameProfileView = findViewById(R.id.tv_usernameProfileView);
        Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable(R.drawable.worldkie_default));
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        ib_profileView.setImageBitmap(bitmap1);
        personalizarImagen(bitmap1);
    }
    public void personalizarImagen(Bitmap bitmap) {
        // Crear un RoundedBitmapDrawable para la imagen circular
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedDrawable.setCircular(true); // Aseguramos que sea circular

        // Establecer la imagen redondeada en el ImageButton
        ib_profileView.setImageDrawable(roundedDrawable);

        // El fondo debe ser un borde redondeado, ajustado dentro del ImageButton
        Drawable drawableBorder = getResources().getDrawable(R.drawable.border_register);
        drawableBorder.setTint(Color.RED); // Color del borde (puedes cambiarlo)

        // Ajustar el borde para que se ajuste bien al contorno del ImageButton
        ib_profileView.setBackground(drawableBorder);
        ib_profileView.setPadding(15, 15, 15, 15); // Añadir padding para el borde visible

        // Asegurarnos de que la imagen esté contenida dentro del borde sin sobresalir
        ib_profileView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta la imagen para que quede dentro del borde
    }
}