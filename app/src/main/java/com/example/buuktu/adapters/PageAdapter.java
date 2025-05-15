package com.example.buuktu.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.buuktu.views.CharacterkiesSearch;
import com.example.buuktu.views.ScenariokiesSearch;
import com.example.buuktu.views.StuffkiesSearch;
import com.example.buuktu.views.UserkiesSearch;
import com.example.buuktu.views.WorldkiesSearch;

public class PageAdapter
        extends FragmentStateAdapter {
    public PageAdapter(@NonNull FragmentActivity fragmentActivity ) {
        super(fragmentActivity);
    }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Determine which fragment to show based on position
            switch (position) {
                case 0:
                    return new WorldkiesSearch(); // First page
                case 1:
                    return new CharacterkiesSearch(); // Second page
                case 2:
                    return new StuffkiesSearch();
                case 3:
                    return new UserkiesSearch();
                case 4:
                    return new ScenariokiesSearch();
            }
            return new WorldkiesSearch();
        }
        @Override
        public int getItemCount() {
            return 5; // Number of fragments (pages)
        }
}