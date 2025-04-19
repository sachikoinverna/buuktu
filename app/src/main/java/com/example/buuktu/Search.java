package com.example.buuktu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.buuktu.adapters.PageAdapter;
import com.example.buuktu.dialogs.InfoFutureFunctionDialog;
import com.example.buuktu.views.MainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    ImageButton ib_save;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PageAdapter pageAdapter;
    SearchView sv_search_main;
    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance() {
        return new Search();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        ImageButton backButton = mainActivity.getBackButton();
        backButton.setVisibility(View.GONE);
        ib_save = mainActivity.getIb_save();
        ib_save.setVisibility(View.GONE);
        initComponents(view);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setUserInputEnabled(false);
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setAlpha(0.5f+Math.abs(position)*0.5f);
            }
        });

        viewPager.setAdapter(pageAdapter);
        viewPager.setUserInputEnabled(true);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Worldkies");
                    break;
                case 1:
                    tab.setText("Characterkies");
                    break;
                case 2:
                    tab.setText("Stuffkies");
                    break;
                case 3:
                    tab.setText("Userkies");
                    break;
            }
        }).attach();
        viewPager.setPageTransformer(null);

        return view;
    }
    private void initComponents(View view){
        sv_search_main = view.findViewById(R.id.sv_search_main);
        InfoFutureFunctionDialog futureFunctionDialog = new InfoFutureFunctionDialog(getContext());
        sv_search_main.setFocusable(false);
        sv_search_main.setIconifiedByDefault(false); // Evita que el SearchView se colapse
        sv_search_main.clearFocus();

        // Interceptar todos los clics para abrir el diálogo
        sv_search_main.setOnClickListener(v -> futureFunctionDialog.show());
        sv_search_main.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                futureFunctionDialog.show();
                v.clearFocus();
            }
        });

        tabLayout = view.findViewById(R.id.tbl_search);
        viewPager = view.findViewById(R.id.vp_search);
        pageAdapter = new PageAdapter(requireActivity());

    }
}