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
            }
            return new WorldkiesSearch();
        }
        @Override
        public int getItemCount() {
            return 4; // Number of fragments (pages)
        }
}