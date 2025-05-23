package com.example.buuktu.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.R;

public class NavigationUtils {
    public static void goBack(FragmentManager fragmentManager, FragmentActivity activity) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            activity.onBackPressed();
        }
    }
    public static void goNewFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    public static void goNewFragmentWithBundle(Bundle bundle,FragmentManager fragmentManager,Fragment fragment){
        fragment.setArguments(bundle);
        goNewFragment(fragmentManager, fragment);
    }
}
