package com.example.buuktu.views;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.Notes;
import com.example.buuktu.Notikies;
import com.example.buuktu.ProfileView;
import com.example.buuktu.R;
import com.example.buuktu.Search;
import com.example.buuktu.broadcastReceiver.WordNotificationReceiver;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.dialogs.InfoWorldkiesDialog;
import com.example.buuktu.listeners.OnDialogInfoClickListener;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.FirebaseAuthUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnDialogInfoClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private ImageButton ib_info,ib_back,ib_self_profile,ib_save;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String UID;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://buuk-tu-users");
    InfoWorldkiesDialog infoWorldkiesDialog;
    InfoFutureFunctionDialog infoFutureFunctionDialog;
    FragmentManager fragmentManager;
    NavigationView navigationView;
    Toolbar toolbar;
int colorEntero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //inicialize();
        initComponents();
        scheduleDailyNotification();

        fragmentManager = getSupportFragmentManager();
         colorEntero = Color.parseColor("#5f5a7c");
        infoFutureFunctionDialog = new InfoFutureFunctionDialog(this);
        UID = FirebaseAuth.getInstance().getUid();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        scheduleDailyNotification();
        infoWorldkiesDialog = new InfoWorldkiesDialog(this);
        infoWorldkiesDialog.setOnDialogClickListener(this);
        infoFutureFunctionDialog.setOnDialogClickListener(this);
        ib_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment  = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(fragment instanceof Home){

                    mostrarInfoWorldkies(v);
                } else if (fragment instanceof Search) {

                } else if (fragment instanceof Inspo){

                } else if (fragment instanceof Notikies) {

                } else if (fragment instanceof Notes){

                }
            }
        });
        setSupportActionBar(toolbar);
        getProfilePhoto();
        // Configuración del ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ib_self_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawableUtils.personalizarImagenCircleButton(getApplicationContext(), DrawableUtils.drawableToBitmap(ib_self_profile.getDrawable()), ib_self_profile, colorEntero,true);
                ProfileView profileView = new ProfileView();
                Bundle bundle = new Bundle();
                bundle.putString("mode","self");
                profileView.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileView)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Configuración del BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new Home())
                            .addToBackStack(null)
                            .commit();
                } else if (id == R.id.search) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new Search())
                            .addToBackStack(null)
                            .commit();
                } else if (id == R.id.inspo) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new Inspo())
                            .addToBackStack(null)
                            .commit();
                } else if (id == R.id.notifications) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new Notikies())
                            .addToBackStack(null)
                            .commit();
                } else if (id == R.id.messages) {
                   infoFutureFunctionDialog.show();
                }
                return true;
            }
        });
        // Cargar el fragmento inicial solo si es la primera creación de la actividad
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Home()).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) // Ejemplo de animación
                    .addToBackStack(null)
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.home); // Selecciona el item "Home" por defecto
        }
    }

    private void getProfilePhoto(){

        ib_self_profile.setVisibility(View.INVISIBLE); // Hacerlo ligeramente transparente al principio
        db.collection("Users").document(UID).addSnapshotListener((queryDocumentSnapshot, e) -> {
            boolean photo_default = queryDocumentSnapshot.getBoolean("photo_default");
            if(photo_default) {
                String id_photo = queryDocumentSnapshot.getString("photo_id");
                int resId = getResources().getIdentifier(id_photo, "mipmap", getPackageName());

                if (resId != 0) {
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), resId);
                    ib_self_profile.setVisibility(View.VISIBLE); // Hacerlo ligeramente transparente al principio
                    startCircularReveal(drawable);
                    ib_self_profile.setImageDrawable(drawable);
                    DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(drawable), ib_self_profile, colorEntero, false);

                } else {
                    Log.e("DRAWABLE", "Recurso no encontrado: " + id_photo);
                }
            }else{
                StorageReference userFolderRef = FirebaseStorage.getInstance("gs://buuk-tu-users").getReference(UID);//.child().child(UID);

                userFolderRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        if (item.getName().startsWith("profile")) {
                            item.getBytes(5 * 1024 * 1024).addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 80, 80, false);

                                RoundedBitmapDrawable circularDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapScaled);
                                circularDrawable.setCircular(true); // Esto hace que la imagen sea circular
                                ib_self_profile.setVisibility(View.VISIBLE); // Hacerlo ligeramente transparente al principio
                                startCircularReveal(circularDrawable);
                                DrawableUtils.personalizarImagenCircleButton(this, bitmapScaled, ib_self_profile, colorEntero, false);
                            });
                            break;
                        }
                    }
                });

            }
        });
    }
    private void startCircularReveal(Drawable finalDrawable) {
        ib_self_profile.setImageDrawable(finalDrawable);
        ib_self_profile.setAlpha(1f);

        // Solo ejecutar la animación en dispositivos con API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtener el centro del ImageButton
            int centerX = ib_self_profile.getWidth() / 2;
            int centerY = ib_self_profile.getHeight() / 2;

            // Calcular el radio final (el círculo más grande que puede caber dentro del ImageButton)
            float finalRadius = Math.max(ib_self_profile.getWidth(), ib_self_profile.getHeight());

            // Crear el Animator para la revelación circular
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    ib_self_profile, centerX, centerY, 0, finalRadius);
            circularReveal.setDuration(500); // Duración de la animación en milisegundos

            // Iniciar la animación
            circularReveal.start();
        }
    }
    private void initComponents(){
        ib_self_profile = findViewById(R.id.ib_self_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        ib_info = findViewById(R.id.ib_info);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        ib_info = findViewById(R.id.ib_info);
        ib_back = findViewById(R.id.ib_back);
        ib_save = findViewById(R.id.ib_save);
    }
    public ImageButton getBackButton(){
        return ib_back;
    }

    public ImageButton getIb_save() {
        return ib_save;
    }

    private void scheduleDailyNotification() {
        Intent intent = new Intent(this, WordNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
    private void mostrarInfoWorldkies(View view){
        infoWorldkiesDialog.show();
    }
   /* private void mostrarInfoInspo(View view){
        infoInspoDialog.show();
    }
    private void mostrarInfoChallenge(View view){
        infoChallengeDialog.show();
    }
    private void mostrarNotekieChallenge(View view){
        infoNotekieDialog.show();
    }
    private void mostrarNotikieChallenge(View view){
        infoNotikieDialog.show();
    }*/
    /*private void mostrarInfoInspo(View view){
        infoInspoDialog.show();
    }
    private void mostrarInfoInspo(View view){
        infoInspoDialog.show();
    }*/
    private void mostrarFutureFunction(View view){
        infoWorldkiesDialog.show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();        } else if (item.getItemId() == R.id.nav_logout) {
            FirebaseAuthUtils.signOut();
            finishAffinity();
            startActivity(new Intent(this, Login.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onAccept() {

    }

    @Override
    public void onCancel() {
        infoWorldkiesDialog.dismiss();
    }
}
