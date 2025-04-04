package com.example.buuktu.views;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.buuktu.broadcastReceiver.WordNotificationReceiver;
import com.example.buuktu.Notes;
import com.example.buuktu.R;
import com.example.buuktu.Search;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.dialogs.InfoWorldkiesDialog;
import com.example.buuktu.listeners.OnDialogInfoClickListener;
import com.example.buuktu.models.UserkieModel;
import com.example.buuktu.utils.BitmapUtils;
import com.example.buuktu.utils.PermissionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnDialogInfoClickListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private ImageButton iv_profileView,ib_info;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String UID;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    private UserkieModel userkieModel;
    FloatingActionButton floatingActionButton;
    InfoWorldkiesDialog infoWorldkiesDialog;
    InfoFutureFunctionDialog infoFutureFunctionDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicialize();
        // Configuración de la Toolbar
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileView.class));
            }
        });
       /* Intent intent = new Intent(this, WordNotificationReceiver.class);
        sendBroadcast(intent);*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        iv_profileView = findViewById(R.id.iv_profileView);
        ib_info = findViewById(R.id.ib_info);
        infoFutureFunctionDialog = new InfoFutureFunctionDialog(this);
        UID = FirebaseAuth.getInstance().getUid();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        /*CollectionReference dbUsers = db.collection("Users");
        dbUsers.document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getBoolean("photo_default")){
                    Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable(R.drawable.worldkie_default));
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    personalizarImagen(bitmap1);
                }else {*/
                    StorageReference storageReference = firebaseStorage.getReference().child(UID);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                        //ib_profileView.setImageBitmap(bitmap1);
                        personalizarImagen(bitmap1);
                        int strokeWidth = 5;
                        int strokeColor = Color.parseColor("#03dc13");
                        int fillColor = Color.parseColor("#ff0000");
                        GradientDrawable gD = new GradientDrawable();
                        gD.setColor(fillColor);
                        gD.setShape(GradientDrawable.OVAL);
                        gD.setStroke(strokeWidth, strokeColor);
                        floatingActionButton.setBackground(gD);
                        //iv_profileView.set(drawable);
                    }).addOnFailureListener(exception ->
                            Toast.makeText(getApplicationContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show()
                    );
                //}
            //}
        //});
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        iv_profileView.startAnimation(rotation);
       // scheduleDailyNotification();
        scheduleTestNotification();
        /*
        // Realizar una tarea en segundo plano (por ejemplo, usando un hilo o AsyncTask)
new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);  // Simula un proceso largo de 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Una vez que el proceso termine, detener la animación y ocultar la vista
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animImageView.clearAnimation();  // Detener la animación
                animImageView.setVisibility(View.GONE);  // Ocultar la vista
            }
        });
    }
}).start();
         */
      //  PermissionUtils.NotifyPermission(this);
       // PermissionUtils.NotifyWordOfTheyDay(this);
        iv_profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado",Toast.LENGTH_SHORT).show();
            }
        });
        ib_info = findViewById(R.id.ib_info);
        infoWorldkiesDialog = new InfoWorldkiesDialog(this);
        infoWorldkiesDialog.setOnDialogClickListener(this);
        infoFutureFunctionDialog.setOnDialogClickListener(this);
        ib_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarInfoWorldkies(v);
            }
        });
        setSupportActionBar(toolbar);
        // Inicialización del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configuración del ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configuración del BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    replaceFragment(new Home());
                }else if (id == R.id.search) {
                    replaceFragment(new Search());
                    //Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.inspo) {
                    replaceFragment(new Inspo());
                    //infoFutureFunctionDialog.show();
                } else if (id == R.id.notifications) {
                  //  PeriodWordsDialog periodWordsDialog = new PeriodWordsDialog(MainActivity.this);
                  //  periodWordsDialog.show();
                  //  PeriodNumbersDialog periodNumbersDialog = new PeriodNumbersDialog(MainActivity.this);
                  //  periodNumbersDialog.show();
                    replaceFragment(new Notes());
                    //Intent intent = new Intent(MainActivity.this, Notes.class);
                   // startActivity(intent);
                    //infoFutureFunctionDialog.show();
                } else if (id == R.id.messages){
                    infoFutureFunctionDialog.show();
                }
                return true;
            }
        });

        // Cargar el fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(new Home());
        }
    }
    public void obtenerImagen(){
        if (userkieModel.isPhoto_default()) {
            //putDefaultImage();
        } else {
            /*StorageReference storageRef = storage.getReference().child(worldkieModel.getUID());
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapUtils.convertCompressedByteArrayToBitmap(bytes);
                Drawable drawable = new BitmapDrawable(createWorldkie.getResources(), bitmap);
                createWorldkie.getIB_profile_photo().setImageDrawable(drawable);
                worldkieModel.setPhoto(drawable);
            }).addOnFailureListener(exception ->
                    Toast.makeText(createWorldkie, "Error al cargar imagen", Toast.LENGTH_SHORT).show()
            );*/
        }
    }
    private void scheduleDailyNotification() {
        Intent intent = new Intent(this, WordNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 30); // En lugar de set a las 00:00
*/

        // Si la hora ya pasó hoy, programa para mañana
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

       /* alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );*/
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
    private void scheduleTestNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WordNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10); // Noti en 10 segundos

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Log.d("NotiTest", "Alarma programada para: " + calendar.getTime().toString());
    }

    public void personalizarImagen(Bitmap bitmap){
        //Canvas canvas = new Canvas(circularBitmap);
        //bt_chooseImage.setBor

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedDrawable.setCircular(true);
        roundedDrawable.setCornerRadius(bitmap.getHeight());
        floatingActionButton.setImageDrawable(roundedDrawable);
        floatingActionButton.setBackgroundColor(Color.RED);
        Drawable drawableBorder = getResources().getDrawable(R.drawable.border_register);
        drawableBorder.setTint(Color.RED);
        floatingActionButton.setBackground(drawableBorder);
        floatingActionButton.setPadding(15, 15, 15, 15); // Añadir padding para el borde visible
        floatingActionButton.setScaleType(ImageView.ScaleType.CENTER_CROP); // Ajusta la imagen para que quede dentro del borde
        //bt_chooseImage.set
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    private void mostrarInfoWorldkies(View view){
        infoWorldkiesDialog.show();
    }
    private void mostrarFutureFunction(View view){
        infoWorldkiesDialog.show();
    }
    private void inicialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.w(TAG, "onAuthStateChanged - Logueado");

                } else {
                    Log.w(TAG, "onAuthStateChanged - Cerro sesion");
                }
            }
        };
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            replaceFragment(new SettingsFragment());
        }else if (item.getItemId() == R.id.nav_logout) {
            firebaseAuth.signOut();
            finishAffinity();
            startActivity(new Intent(this, Login.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAccept() {

    }

    @Override
    public void onCancel() {
        infoWorldkiesDialog.dismiss();
    }
}
