package com.example.campusexpensemanager.main.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.campusexpensemanager.main.Fragment.CategoryFragment;
import com.example.campusexpensemanager.main.Fragment.ExpenseFragment;
import com.example.campusexpensemanager.main.Fragment.HomeFragment;
import com.example.campusexpensemanager.main.Fragment.OverviewFragment;

public class DashBroad_Adapter extends FragmentStateAdapter {
    public DashBroad_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new HomeFragment();
        }
        if(position == 1){
            return new CategoryFragment();
        }
        if(position == 2){
            return new ExpenseFragment();
        }
        if(position == 3){
            return new OverviewFragment();
        }
        else {
            return new HomeFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
