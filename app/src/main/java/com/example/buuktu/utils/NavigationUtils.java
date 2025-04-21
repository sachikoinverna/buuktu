package com.example.buuktu.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.buuktu.R;
import com.example.buuktu.views.CreateEditWorldkie;

public class NavigationUtils {
    public static void goBack(FragmentManager fragmentManager, FragmentActivity activity) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Realiza alguna acción si no hay nada en la pila de retroceso
            // Por ejemplo, cerrar la actividad:
            activity.onBackPressed();
        }
    }
}
