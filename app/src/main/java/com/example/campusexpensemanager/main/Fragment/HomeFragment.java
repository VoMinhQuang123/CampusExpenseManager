package com.example.campusexpensemanager.main.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Adapter.Category_Adapter;
import com.example.campusexpensemanager.main.Adapter.Overview_Adapter;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ArrayList<Category_Expense_Model> budgetModels;
    private Overview_Adapter budget;
    private Category_Expense_Model model;
    private Category_Expense_Repository repository;
    private RecyclerView budgetRCC;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        PieChart pieChart = view.findViewById(R.id.pieChart);
//        repository = new Category_Expense_Repository(getActivity());
//        budgetRCC = view.findViewById(R.id.rvBudget);
//        budgetModels = new ArrayList<>();
//        repository = new Category_Expense_Repository(getActivity());
//        budgetModels= repository.getListBudget();
//        budget = new Overview_Adapter(budgetModels, getContext());
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        budgetRCC.setLayoutManager(manager);
//        budgetRCC.setAdapter(budget);
//
//
//        Map<Integer, Integer> categoryExpenses = repository.getExpenseByCategory(0);
//        int totalAll = 0;
//        for (int value : categoryExpenses.values()) {
//            totalAll += value;
//        }
//        List<PieEntry> entries = new ArrayList<>();
//        for (Map.Entry<Integer, Integer> entry : categoryExpenses.entrySet()) {
//            float percent = (float) entry.getValue() * 100 / totalAll;
//            PieEntry pieEntry = new PieEntry(percent, "" + entry.getKey());
//            pieEntry.setData(entry.getKey()); // ✅ Gán categoryId vào PieEntry
//            entries.add(pieEntry);
//        }
//        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        PieData data = new PieData(dataSet);
//        pieChart.setData(data);
//        pieChart.invalidate(); // refresh chart
//
//
//        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                if (e instanceof PieEntry) {
//                    PieEntry pieEntry = (PieEntry) e;
//                    int categoryId = (int) pieEntry.getData(); // ép kiểu đúng
//                    showExpensesByCategory(categoryId); // gọi hiển thị chi tiết
//                }
//            }
//            @Override
//            public void onNothingSelected() {
//                showAllExpenses();
//
//            }
//        });
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
//    private void showExpensesByCategory(int categoryId) {
//        ArrayList<Category_Expense_Model> filtered = repository.getExpensesByCategoryId(categoryId);
//        budget.setData(filtered);
//        budget.notifyDataSetChanged();
//    }
//
//    private void showAllExpenses() {
//        ArrayList<Category_Expense_Model> filtered = repository.getExpensesByCategoryId(0);
//        budget.setData(filtered);
//        budget.notifyDataSetChanged();
//    }

}