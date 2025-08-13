package com.example.campusexpensemanager.main.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Adapter.DashBroad_Adapter;
import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DashBroadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.view_pager2);
        drawerLayout = findViewById(R.id.draw_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPage();
        Menu menu = navigationView.getMenu();
        MenuItem logout = menu.findItem(R.id.logout);
        MenuItem setting = menu.findItem(R.id.menu_setting);

        setting.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(DashBroadActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("userId");
                editor.clear();
                editor.apply();

                Intent intent = new Intent(DashBroadActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                return false;
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_home){
                viewPager2.setCurrentItem(0);
            }
            if(item.getItemId() == R.id.menu_budget){
                viewPager2.setCurrentItem(1);
            }
            if(item.getItemId() == R.id.menu_expense){
                viewPager2.setCurrentItem(2);
            }
            if(item.getItemId() == R.id.menu_income){
                viewPager2.setCurrentItem(3);
            }
            if(item.getItemId() == R.id.menu_overview){
                viewPager2.setCurrentItem(4);
            }
            return true;
        });
    }
    private void setupViewPage(){
        DashBroad_Adapter view = new DashBroad_Adapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(view);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }
                if(position == 1){
                    bottomNavigationView.getMenu().findItem(R.id.menu_budget).setChecked(true);
                }
                if(position == 2){
                    bottomNavigationView.getMenu().findItem(R.id.menu_expense).setChecked(true);
                }
                if(position == 3){
                    bottomNavigationView.getMenu().findItem(R.id.menu_income).setChecked(true);
                }
                if(position == 4){
                    bottomNavigationView.getMenu().findItem(R.id.menu_overview).setChecked(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_home){
            viewPager2.setCurrentItem(0);
        }
        if(item.getItemId() == R.id.menu_budget){
            viewPager2.setCurrentItem(1);
        }
        if(item.getItemId() == R.id.menu_expense){
            viewPager2.setCurrentItem(2);
        }
        if(item.getItemId() == R.id.menu_income){
            viewPager2.setCurrentItem(3);
        }
        if(item.getItemId() == R.id.menu_overview){
            viewPager2.setCurrentItem(4);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
