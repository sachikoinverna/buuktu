package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.SettingsAdapter;
import com.example.buuktu.models.SettingModel;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {
    MainActivity mainActivity;
    private RecyclerView rc_settings;
    private final ArrayList<SettingModel> dataSet = new ArrayList<>();
    private SearchView searchView;
    private final ArrayList<SettingModel> filteredDataSet = new ArrayList<>();
    SettingsAdapter settingsAdapter;
    ImageButton backButton,ib_save,ib_profile_superior;
    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_settings, container, false);// Inflate the layout for this fragment
        initComponents(v);
        setSearchViewProperties();
        setVisibility();

        setRecyclerView();
        setListeners();
        return v;
    }
    private void setSearchViewProperties(){
        searchView.setIconifiedByDefault(false); // Para que el SearchView esté expandido por defecto
    }
    private void initComponents(View v){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        rc_settings = v.findViewById(R.id.rc_settings);
        searchView = v.findViewById(R.id.searchView);
    }
    private void setRecyclerView() {
        dataSet.clear();
        filteredDataSet.clear();
        Drawable drawableProfile = ContextCompat.getDrawable(mainActivity, R.drawable.twotone_manage_accounts_24);
        Drawable drawableAccount = ContextCompat.getDrawable(mainActivity, R.drawable.twotone_admin_panel_settings_24);

        dataSet.add(new SettingModel(mainActivity.getString(R.string.profile),drawableProfile));
        dataSet.add(new SettingModel(mainActivity.getString(R.string.account),drawableAccount));
        filteredDataSet.addAll(dataSet);
        updateRecyclerView(filteredDataSet);
    }
    private void setVisibility(){
        backButton.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
        ib_save.setVisibility(View.GONE);
    }
    private void setListeners(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }
    private void filterList(String query) {
        filteredDataSet.clear();

        // Filtrar los elementos de la lista
        if (query.isEmpty()) {
            // Si el campo de búsqueda está vacío, muestra todos los elementos
            filteredDataSet.addAll(dataSet);
        } else {
            // Filtrar los elementos que contienen el texto de la búsqueda
            for (SettingModel item : dataSet) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredDataSet.add(item);

                }
            }
        }
        updateRecyclerView(filteredDataSet);
        // Notificar al adaptador que los datos han cambiado
        settingsAdapter.notifyDataSetChanged();
    }
    private void updateRecyclerView(ArrayList<SettingModel> settingModels){
        settingsAdapter = new SettingsAdapter(settingModels,mainActivity,getParentFragmentManager());
        rc_settings.setAdapter(settingsAdapter);
        rc_settings.setLayoutManager(new GridLayoutManager(mainActivity,2));
    }
}