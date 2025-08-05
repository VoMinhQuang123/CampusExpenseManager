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
import com.example.campusexpensemanager.main.Activity.Recurring.AddRecurringActivity;
import com.example.campusexpensemanager.main.Activity.Tracking.AddTrackingActivity;
import com.example.campusexpensemanager.main.Adapter.Category_Adapter;
import com.example.campusexpensemanager.main.Adapter.Recurring_Adapter;
import com.example.campusexpensemanager.main.Adapter.Tracking_Adapter;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;
import com.example.campusexpensemanager.main.Repository.Category_Expense_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView ViewExpense;
    private ArrayList<Expense_Tracking_Model> Models;
    private Tracking_Adapter budget;
    private Expense_Tracking_Model model;
    private Expense_Tracking_Repository repository;
    private RecyclerView RCC;

    public TrackingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackingFragment newInstance(String param1, String param2) {
        TrackingFragment fragment = new TrackingFragment();
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
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        View view =  inflater.inflate(R.layout.fragment_tracking, container, false);
        Button btnCreate = view.findViewById(R.id.btnCeaterTracking);
        RCC = view.findViewById(R.id.rvBudget);
        Models = new ArrayList<>();
        repository = new Expense_Tracking_Repository(getActivity());
        Models= repository.getListTracking(userId);
        budget = new Tracking_Adapter(Models, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RCC.setLayoutManager(manager);
        RCC.setAdapter(budget);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTrackingActivity.class);
                startActivity(intent);
            }
        });





        return view;
    }
}