package com.example.campusexpensemanager.main.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Adapter.Category_Adapter;
import com.example.campusexpensemanager.main.Activity.Category.AddCategoryActivity;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView ViewExpense;
    private ArrayList<Category_Expense_Model> budgetModels;
    private Category_Adapter budget;
    private Category_Expense_Model model;
    private Category_Expense_Repository repository;
    private RecyclerView budgetRCC;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        Button btnCreateBudget = view.findViewById(R.id.btnbudgetCeater);
        budgetRCC = view.findViewById(R.id.rvBudget);
        budgetModels = new ArrayList<>();
        repository = new Category_Expense_Repository(getActivity());
        budgetModels= repository.getListBudget(userId);
        budget = new Category_Adapter(budgetModels, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetRCC.setLayoutManager(manager);
        budgetRCC.setAdapter(budget);



        ViewExpense = view.findViewById(R.id.viewExpense);
        int total = repository.getTotalExpense(userId);
        ViewExpense.setText("Amounts: " + total + " VNĐ");


//        budget.setOnClickListener(new Budget.OnClickListener() {
//            @Override
//            public void onClick(int position) {
//                Category_Expense_Model model1 = budgetModels.get(position);
//                String name = model1.getName();
//                int expenseve = model1.getExpensive();
//                int id = model1.getId();
//                String des = model1.getDescription();
//                // use intent + bundle sang edit
//                // Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), EditBudgetActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("ID_BUDGET", id);
//                bundle.putString("NAME_BUDGET", name);
//                bundle.putInt("MONEY_BUDGET", expenseve);
//                bundle.putString("DESCRIPTION", des);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        btnCreateBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}