package com.example.buuktu.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.R;
import com.example.buuktu.dialogs.InfoGeneralDialog;
import com.example.buuktu.utils.DrawableUtils;
import com.example.buuktu.utils.EfectsUtils;
import com.example.buuktu.utils.FirebaseAuthUtils;
import com.example.buuktu.utils.NavigationUtils;
import com.example.buuktu.utils.NotikiesUtils;
import com.example.buuktu.utils.PermissionUtils;
import com.example.buuktu.utils.UIUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean isProfileImageLoaded = false;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private ImageButton ib_info,ib_back,ib_self_profile,ib_save;
    private String UID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorageUsers = FirebaseStorage.getInstance("gs://buuk-tu-users"),
    firebaseStorageWorldkies= FirebaseStorage.getInstance("gs://buuk-tu-worldkies"),
            firebaseStorageCharacterkies = FirebaseStorage.getInstance("gs://buuk-tu-characterkies")
                    ,firebaseStorageStuffkies = FirebaseStorage.getInstance("gs://buuk-tu-stuffkies")
            ,firebaseStorageScenariokies = FirebaseStorage.getInstance("gs://buuk-tu-scenariokies");
    private InfoGeneralDialog infoGeneralDialog;
    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final CollectionReference collectionNotekies = db.collection("Notekies"),
            collectionUsers=db.collection("Users"),
            notikiesCollection = db.collection("Notikies")
            ,collectionScenariokies = db.collection("Scenariokies"),
            collectionStuffkies = db.collection("Stuffkies"),
            collectionCharacterkies = db.collection("Characterkies"),
            collectionWorldkies = db.collection("Worldkies");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        UIUtils.hideSystemUI(this);

        initComponents();
        scheduleDailyNotification();



        setFirebaseSettings();
        scheduleDailyNotification();
        setSupportActionBar(toolbar);
        getProfilePhoto();
        // Configuración del ActionBarDrawerToggle

        // Configuración del BottomNavigationView
            setListeners();
        if (savedInstanceState == null) {
            loadHome();
        }
        // Cargar el fragmento inicial solo si es la primera creación de la actividad

    }
    private void setFirebaseSettings(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    private void loadHome(){
        NavigationUtils.goNewFragment(fragmentManager, new Home());
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    private void setListeners(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                NavigationUtils.goNewFragment(fragmentManager, new Home());
            } else if (id == R.id.search) {
                NavigationUtils.goNewFragment(fragmentManager, new Search());
            } else if (id == R.id.inspo) {
                NavigationUtils.goNewFragment(fragmentManager, new Inspo());
            } else if (id == R.id.notifications) {
                NavigationUtils.goNewFragment(fragmentManager, new Notikies());
            } else if (id == R.id.messages) {
                showInfoDialog( "future_function");
            }
            return true;
        });
        ib_info.setOnClickListener(v -> getInfo());
    }
    public void getProfileUser(View view){
                Bundle bundle = new Bundle();
                bundle.putString("mode","self");
        NavigationUtils.goNewFragmentWithBundle(bundle,fragmentManager,new ProfileView());
    }
    public void getInfo(){
            Fragment fragment  = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof Search) showInfoDialog("search");
            else if (fragment instanceof Inspo) showInfoDialog("inspo");
            else if (fragment instanceof InspoDesafios) showInfoDialog("challenges");
            else if(fragment instanceof Note) showInfoDialog( "notekies");
            else if(fragment instanceof SettingsFragment||fragment instanceof AccountSettings||fragment instanceof ProfileSettings)showInfoDialog("settings");
            else if (fragment instanceof Notikies) showInfoDialog("notikies");
            else if (fragment instanceof Notes) showInfoDialog("notekies");
            else if (fragment instanceof Home || fragment instanceof WorldkieMenu || fragment instanceof WorldkieView) showInfoDialog("worldkies");
            else if (fragment instanceof Characterkies || fragment instanceof CharacterkieView) showInfoDialog("characterkies");
            else if (fragment instanceof Stuffkies || fragment instanceof StuffkieView) showInfoDialog("stuffkies");
            else if (fragment instanceof Scenariokies || fragment instanceof Scenariokie) showInfoDialog("scenariokies");
            else if (fragment instanceof ProfileView) showInfoDialog("profile");

    }

    public void showInfoDialog(String mode){
        infoGeneralDialog = new InfoGeneralDialog(this,mode);
        infoGeneralDialog.show();
    }
    private void getProfilePhoto(){
if(!isProfileImageLoaded) {
    ib_self_profile.setVisibility(View.INVISIBLE); // Hacerlo ligeramente transparente al principio
    collectionUsers.document(UID).addSnapshotListener((queryDocumentSnapshot, e) -> {
        if (queryDocumentSnapshot.getBoolean("photo_default")) {
            int resId = getResources().getIdentifier(queryDocumentSnapshot.getString("photo_id"), "mipmap", getPackageName());
            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(this, resId);
                DrawableUtils.personalizarImagenCircleButton(this, DrawableUtils.drawableToBitmap(drawable), ib_self_profile, R.color.purple1);
                ib_self_profile.setVisibility(View.VISIBLE); // Hacerlo ligeramente transparente al principio

                EfectsUtils.startCircularReveal(drawable, ib_self_profile);
            }
        } else {
            firebaseStorageUsers.getReference().listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (item.getName().startsWith("profile")) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            DrawableUtils.personalizarImagenCircleButton(this, uri, ib_self_profile, R.color.purple1);
                            ib_self_profile.setVisibility(View.VISIBLE); // Hacerlo ligeramente transparente al principio
                            EfectsUtils.startCircularReveal(ib_self_profile.getDrawable(), ib_self_profile);
                        });
                        break;
                    }
                }
            });

        }
    });
    isProfileImageLoaded = true;

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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        UID = firebaseAuth.getUid();
    }

    public FirebaseStorage getFirebaseStorageCharacterkies() {
        return firebaseStorageCharacterkies;
    }

    public FirebaseStorage getFirebaseStorageUsers() {
        return firebaseStorageUsers;
    }

    public CollectionReference getCollectionNotekies() {
        return collectionNotekies;
    }

    public CollectionReference getCollectionUsers() {
        return collectionUsers;
    }

    public ImageButton getBackButton(){
        return ib_back;
    }

    public ImageButton getIb_save() {
        return ib_save;
    }

    public FirebaseStorage getFirebaseStorageScenariokies() {
        return firebaseStorageScenariokies;
    }

    public ImageButton getIb_self_profile() {
        return ib_self_profile;
    }

    public String getUID() {
        return UID;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public CollectionReference getCollectionWorldkies() {
        return collectionWorldkies;
    }

    public CollectionReference getNotikiesCollection() {
        return notikiesCollection;
    }

    public CollectionReference getCollectionScenariokies() {
        return collectionScenariokies;
    }

    public CollectionReference getCollectionStuffkies() {
        return collectionStuffkies;
    }

    public CollectionReference getCollectionCharacterkies() {
        return collectionCharacterkies;
    }

    public FirebaseStorage getFirebaseStorageStuffkies() {
        return firebaseStorageStuffkies;
    }

    public FirebaseStorage getFirebaseStorageWorldkies() {
        return firebaseStorageWorldkies;
    }

    private void scheduleDailyNotification() {
        PermissionUtils.NotifyPermission(this);
        NotikiesUtils.setDailyAlarm(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Comprueba si se ha presionado el botón de retroceso.

        if (item.getItemId() == R.id.nav_settings) {
            NavigationUtils.goNewFragment(fragmentManager, new SettingsFragment());

        } else if (item.getItemId() == R.id.nav_logout) {
            FirebaseAuthUtils.signOut();
            finishAffinity();
            startActivity(new Intent(this, Login.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si el usuario actual ya ha iniciado sesión
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // Si el usuario no ha iniciado sesión, redirige a la actividad de Login
            startActivity(new Intent(this, Login.class));
            finish(); // Cierra MainActivity para que el usuario no pueda volver atrás sin iniciar sesión
        }
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof Home) {
        } else {
            super.onBackPressed();
        }
    }
}
