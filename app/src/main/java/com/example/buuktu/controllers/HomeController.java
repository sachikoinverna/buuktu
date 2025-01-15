package com.example.buuktu.controllers;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.buuktu.R;
import com.example.buuktu.views.CreateWorldkie;
import com.example.buuktu.views.Home;

public class HomeController implements View.OnClickListener {
    private final Home home;

    public HomeController(Home home) {
        this.home = home;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.fb_parentWorldkies){
                if (!home.isAllFabsVisible()) {
                    home.getFb_add().setVisibility(View.VISIBLE);
                    home.setAllFabsVisible(true);
                } else {
                    home.getFb_add().setVisibility(View.GONE);
                    home.setAllFabsVisible(false);
                }
            } else if (v.getId()==R.id.fb_addWorldkie) {
            Intent intent = new Intent(home.getContext(), CreateWorldkie.class);
            intent.putExtra("create",true);
            home.startActivity(intent);
        }

    }
}
