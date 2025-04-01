package com.example.buuktu.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.buuktu.FirstFragment;

public class PageAdapter
        extends FragmentPagerAdapter {
    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new FirstFragment();
        /*else if (position == 1)
            fragment = new CourseFragment();
        else if (position == 2)
            fragment = new LoginFragment();
*/
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Algorithm";
        else if (position == 1)
            title = "Courses";
        else if (position == 2)
            title = "Login";
        return title;
    }
}