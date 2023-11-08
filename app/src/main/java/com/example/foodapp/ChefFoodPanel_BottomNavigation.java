package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.foodapp.chefFoodPanel.ChefHomeFragment;
import com.example.foodapp.chefFoodPanel.ChefOrderFragment;
import com.example.foodapp.chefFoodPanel.ChefPendingOrderFragment;
import com.example.foodapp.chefFoodPanel.ChefProfileFragment;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                loadChefFragment(new ChefPendingOrderFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                loadChefFragment(new ChefOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                loadChefFragment(new ChefOrderFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                loadChefFragment(new ChefOrderFragment());
            }
        } else {
            loadChefFragment(new ChefHomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();

        if (itemId == R.id.chefHome) {
            fragment = new ChefHomeFragment();
        } else if (itemId == R.id.PendingOrders) {
            fragment = new ChefPendingOrderFragment();
        } else if (itemId == R.id.Orders) {
            fragment = new ChefOrderFragment();
        } else if (itemId == R.id.chefProfile) {
            fragment = new ChefProfileFragment();
        }
        return loadChefFragment(fragment);
    }

    private boolean loadChefFragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}