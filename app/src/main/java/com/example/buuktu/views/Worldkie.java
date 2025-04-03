package com.example.buuktu.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.CharacterkiesWorldkiePreviewAdapter;
import com.example.buuktu.models.Characterkie;

import java.util.ArrayList;

public class Worldkie extends AppCompatActivity implements CharacterkiesWorldkiePreviewAdapter.ItemClickListener {
    private RecyclerView rc_characterkiesWorldkiePreview;
    private ArrayList<Characterkie> dataSet = new ArrayList<Characterkie>();
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worldkie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rc_characterkiesWorldkiePreview = findViewById(R.id.rc_characterkiePreviewUserSelf);
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        dataSet.add(new Characterkie("12sf","LALA"));
        CharacterkiesWorldkiePreviewAdapter characterkiesWorldkiePreviewAdapter = new CharacterkiesWorldkiePreviewAdapter(dataSet,this);
        rc_characterkiesWorldkiePreview.setAdapter(characterkiesWorldkiePreviewAdapter);
        rc_characterkiesWorldkiePreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
      //  characterkiesWorldkiePreviewAdapter.setOnClickListener(this);
      //  ImageDecoder.Source image1 = ImageDecoder.createSource(this.getContentResolver(),uri);
      //  Drawable drawable = imageView.getDrawable();
      //  Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
      //  personalizarImagen(bitmap);
    }

    @Override
    public void onClick(View view, int position) {

    }
}