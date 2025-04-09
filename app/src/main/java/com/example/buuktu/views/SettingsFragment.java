package com.example.buuktu.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.buuktu.R;
import com.example.buuktu.adapters.SettingsAdapter;
import com.example.buuktu.models.SettingModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements SettingsAdapter.ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SettingsAdapter adapter;
    private RecyclerView rc_settings;
    private ArrayList<SettingModel> dataSet = new ArrayList<SettingModel>();
    private SearchView searchView;
    private ArrayList<SettingModel> filteredDataSet = new ArrayList<SettingModel>();;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_settings, container, false);// Inflate the layout for this fragment
         rc_settings = v.findViewById(R.id.rc_settings);
         searchView = v.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false); // Para que el SearchView esté expandido por defecto
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.twotone_manage_accounts_24);
        dataSet.add(new SettingModel("Perfil",drawable));
        dataSet.add(new SettingModel("Cuenta",drawable));
        SettingsAdapter settingsAdapter = new SettingsAdapter(dataSet,getContext());
        rc_settings.setAdapter(settingsAdapter);
        rc_settings.setLayoutManager(new LinearLayoutManager(getContext()));
        settingsAdapter.setOnClickListener(this);
        adapter = new SettingsAdapter(dataSet,getContext());
        filteredDataSet.addAll(dataSet);
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
        return v;
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

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {
        if (dataSet.get(position).getName().equals("Perfil")){
            System.out.println("Perfil");
        } else if (dataSet.get(position).getName().equals("Account")) {
            System.out.println("Account");
        }
    }
}