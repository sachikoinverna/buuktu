package com.example.buuktu.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkiesUserPreviewAdapter;
import com.example.buuktu.models.Characterkie;
import com.example.buuktu.utils.DrawableUtils;

import java.util.ArrayList;

public class ProfileView extends AppCompatActivity {
 private ImageButton ib_profileView;
 private TextView tv_usernameProfileView;
 ArrayList<Characterkie> characterkieArrayList;
 RecyclerView rc_characterkiePreviewUserSelf,rc_stuffkiePreviewUserSelf,rc_worldkiePreviewUserSelf;
 CharacterkiesUserPreviewAdapter characterkiesUserPreviewAdapter;
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
        rc_worldkiePreviewUserSelf = findViewById(R.id.rc_worldkiePreviewUserSelf);
        rc_stuffkiePreviewUserSelf = findViewById(R.id.rc_stuffkiePreviewUserSelf);
        rc_characterkiePreviewUserSelf = findViewById(R.id.rc_characterkiePreviewUserSelf);

        ib_profileView = findViewById(R.id.ib_profileView);
        tv_usernameProfileView = findViewById(R.id.tv_usernameProfileView);
        /*Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable(R.drawable.worldkie_default));
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        ib_profileView.setImageBitmap(bitmap1);
        personalizarImagen(bitmap1);*/
        DrawableUtils.personalizarImagenCircleButton(this,DrawableUtils.drawableToBitmap(ib_profileView.getDrawable()),ib_profileView,R.color.brownBrown);
        ib_profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        characterkieArrayList = new ArrayList<>();
        characterkieArrayList.add(new Characterkie("232532","lALA"));
        characterkieArrayList.add(new Characterkie("232532","lALSA"));
        characterkieArrayList.add(new Characterkie("232532","lALSFA"));
        characterkieArrayList.add(new Characterkie("232532","lAFLSA"));
        characterkieArrayList.add(new Characterkie("232532","lASLAF"));
        characterkiesUserPreviewAdapter = new CharacterkiesUserPreviewAdapter(characterkieArrayList,this);
        rc_characterkiePreviewUserSelf.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_characterkiePreviewUserSelf.setAdapter(characterkiesUserPreviewAdapter);
    }
    /*public void personalizarImagen(Bitmap bitmap) {
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
    }*/
}