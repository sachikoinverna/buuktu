package com.example.buuktu.adapters;

import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.buuktu.CharacterkiesSearch;
import com.example.buuktu.StuffkiesSearch;
import com.example.buuktu.UserkiesSearch;
import com.example.buuktu.WorldkiesSearch;

public class PageAdapter
        extends FragmentStateAdapter {
    SearchView searchView;
    public PageAdapter(@NonNull FragmentActivity fragmentActivity, SearchView searchView) {
        super(fragmentActivity);
        this.searchView=searchView;
    }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Determine which fragment to show based on position
            switch (position) {
                case 0:
                    return new WorldkiesSearch(searchView); // First page
                case 1:
                    return new CharacterkiesSearch(searchView); // Second page
                case 2:
                    return new StuffkiesSearch(searchView);
                case 3:
                    return new UserkiesSearch(searchView);
            }
            return new WorldkiesSearch(searchView);
        }
        @Override
        public int getItemCount() {
            return 4; // Number of fragments (pages)
        }
}