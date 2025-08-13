package com.example.campusexpensemanager.main.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Adapter.Home_Adapter;
import com.example.campusexpensemanager.main.Model.CategoryData;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  Category_Expense_Repository repository1;
    private Expense_Reccuring_Repository repository2;
    private Expense_Tracking_Repository repository3;
    private Home_Adapter adapter;
    private RecyclerView recyclerView;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        repository1 = new Category_Expense_Repository(getContext());
        repository2 = new Expense_Reccuring_Repository(getContext());
        repository3 = new Expense_Tracking_Repository(getContext());

        recyclerView = view.findViewById(R.id.rvBudget);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            // Xử lý trường hợp chưa có userId (chưa đăng nhập,...)
            return view;
        }

        ArrayList<Category_Expense_Model> categoryList = repository1.getListBudget(userId);
        ArrayList<Expense_Recurring_Model> recurringList = repository2.getListRecurring(userId);
        ArrayList<Expense_Tracking_Model> trackingList = repository3.getListTracking(userId);

        List<CategoryData> categoryDataList = new ArrayList<>();

        for (Category_Expense_Model category : categoryList) {
            int catId = category.getId();
            String catName = category.getName();

            int totalCategory = category.getBudget();

            // Tính tổng chi tiêu Recurring cho category
            double recurringSum = 0;
            for (Expense_Recurring_Model rec : recurringList) {
                if (rec.getCategoryId() == catId) {
                    recurringSum += rec.getExpense();
                }
            }

            // Tính tổng chi tiêu Tracking cho category
            double trackingSum = 0;
            for (Expense_Tracking_Model track : trackingList) {
                if (track.getCategoryId() == catId) {
                    trackingSum += track.getExpense();
                }
            }

            double totalSpent = recurringSum + trackingSum;

            categoryDataList.add(new CategoryData(catName, (int) totalSpent, totalCategory));
        }
        Log.d("HomeFragment", "CategoryDataList size: " + categoryDataList.size());
        for (CategoryData data : categoryDataList) {
            Log.d("HomeFragment", "Category: " + data.getName() + ", TotalSpent: " + data.getTotalSpent() + ", TotalBudget: " + data.getTotalCategory());
        }

        // Tạo và set adapter cho recyclerView
        adapter = new Home_Adapter(getContext(), categoryDataList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}