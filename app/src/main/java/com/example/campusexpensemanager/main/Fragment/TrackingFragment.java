package com.example.campusexpensemanager.main.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Tracking.AddTrackingActivity;
import com.example.campusexpensemanager.main.Activity.Tracking.EditTrackingActivity;
import com.example.campusexpensemanager.main.Adapter.Tracking_Adapter;
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;
import com.example.campusexpensemanager.main.Repository.Expense_Tracking_Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TrackingFragment extends Fragment {

    private Button btnCreate;
    private ArrayList<Expense_Tracking_Model> Models;
    private Tracking_Adapter budget;
    private Expense_Tracking_Repository repository;
    private RecyclerView RCC;

    public TrackingFragment() {
        // Required empty public constructor
    }

    public static TrackingFragment newInstance(String param1, String param2) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    private int loadUserId() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPref.getInt("userId", -1);
    }

    private void initViews(View view) {
        btnCreate = view.findViewById(R.id.btnCreateTracking);
        RCC = view.findViewById(R.id.rvBudget);
        Models = new ArrayList<>();
        repository = new Expense_Tracking_Repository(getActivity());
    }

    private void loadTrackingExpenses(int userId) {
        Models = repository.getListTracking(userId);
    }

    private void setupRecyclerView() {
        budget = new Tracking_Adapter(Models, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RCC.setLayoutManager(manager);
        RCC.setAdapter(budget);
    }

    private void setupCreateButtonListener() {
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTrackingActivity.class);
            startActivity(intent);
        });
    }

    private void setupItemClickListener(int userId) {
        budget.setOnClickListener(position -> {
            Expense_Tracking_Model selectedModel = Models.get(position);
            Intent intent = new Intent(getActivity(), EditTrackingActivity.class);
            Bundle bundle = new Bundle();

            bundle.putInt("ID_TRACKING", selectedModel.getId());
            bundle.putString("NAME_TRACKING", selectedModel.getName());
            bundle.putDouble("MONEY_TRACKING", selectedModel.getExpense());
            bundle.putString("NOTE_TRACKING", selectedModel.getNote());

            bundle.putString("CREATE_AT", formatLocalDateTime(selectedModel.getCreate_at()));
            bundle.putString("UPDATE_AT", formatLocalDateTime(selectedModel.getUpdate_at()));

            bundle.putInt("CATEGORY_ID", selectedModel.getCategoryId());
            bundle.putInt("USER_ID", userId);

            intent.putExtras(bundle);
            startActivity(intent);
        });

        budget.setOnDeleteListener(position -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        Expense_Tracking_Model item = Models.get(position);
                        long res = repository.deleteTrackingById(item.getId());
                        if (res > 0) {
                            Models.remove(position);
                            budget.notifyItemRemoved(position);
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.toLocalDate().toString(); // yyyy-MM-dd
        }
        return "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        int userId = loadUserId();

        initViews(view);
        loadTrackingExpenses(userId);
        setupRecyclerView();
        setupCreateButtonListener();
        setupItemClickListener(userId);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int userId = loadUserId();
        Models = repository.getListTracking(userId);
        if (budget != null) {
            budget.updateData(Models);
        }
    }
}
