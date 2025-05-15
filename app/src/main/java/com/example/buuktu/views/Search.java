package com.example.buuktu.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.buuktu.R;
import com.example.buuktu.adapters.PageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Search extends Fragment {

    private ImageButton ib_save,ib_profile_superior,backButton;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PageAdapter pageAdapter;
    private SearchView sv_search_main;
    private MainActivity mainActivity;
    public Search() {
    }


    public static Search newInstance() {
        return new Search();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        initComponents(view);
        setVisibility();
        setListeners();
        setupViewPager();

        return view;
    }
    private void initComponents(View view){
        mainActivity = (MainActivity) getActivity();
        backButton = mainActivity.getBackButton();
        ib_save = mainActivity.getIb_save();
        ib_profile_superior = mainActivity.getIb_self_profile();
        sv_search_main = view.findViewById(R.id.sv_search_main);
        tabLayout = view.findViewById(R.id.tbl_search);
        viewPager = view.findViewById(R.id.vp_search);
        pageAdapter = new PageAdapter(mainActivity);

    }
    private void setListeners(){
        sv_search_main.setOnClickListener(v -> mainActivity.showInfoDialog("future_function"));
        sv_search_main.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mainActivity.showInfoDialog("future_function");
                v.clearFocus();
            }
        });
    }
    private void setupViewPager(){
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(pageAdapter);
        viewPager.setUserInputEnabled(true);

        String[] tabTitles = {
                getString(R.string.worldkies),
                getString(R.string.characterkies),
                getString(R.string.stuffkies),getString(R.string.userkies),
                getString(R.string.scenariokies)
        };
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabTitles[position])).attach();
    }
    private void setVisibility(){
        backButton.setVisibility(View.GONE);
        ib_save.setVisibility(View.GONE);
        ib_profile_superior.setVisibility(View.VISIBLE);
    }
}