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
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusexpensemanager.R;

import com.example.campusexpensemanager.main.Activity.Recurring.AddRecurringActivity;
import com.example.campusexpensemanager.main.Activity.Recurring.EditRecurringActivity;
import com.example.campusexpensemanager.main.Adapter.Recurring_Adapter;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Repository.Expense_Reccuring_Repository;
import java.time.LocalDateTime;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecurringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecurringFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView ViewExpense;
    private Button btnCreate;
    private ArrayList<Expense_Recurring_Model> Models;
    private Recurring_Adapter budget;
    private Expense_Recurring_Model model;
    private Expense_Reccuring_Repository repository;
    private RecyclerView RCC;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecurringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecurringFragment newInstance(String param1, String param2) {
        RecurringFragment fragment = new RecurringFragment();
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

        View view = inflater.inflate(R.layout.fragment_recurring, container, false);

        // 1. Load user ID from shared preferences
        int userId = loadUserId();

        // 2. Initialize views and recycler view
        initViews(view);

        // 3. Load recurring expense data from repository
        loadRecurringExpenses(userId);

        // 4. Setup RecyclerView adapter and layout manager
        setupRecyclerView();

        // 5. Setup Create button listener to start AddRecurringActivity
        setupCreateButtonListener();

        // 6. Setup click listener for items in RecyclerView
        setupItemClickListener(userId);

        return view;
    }

    private int loadUserId() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPref.getInt("userId", -1);
    }
    private void initViews(View view) {
        btnCreate = view.findViewById(R.id.btnCeaterRecurring);
        RCC = view.findViewById(R.id.rvBudget);
        Models = new ArrayList<>();
        repository = new Expense_Reccuring_Repository(getActivity());
    }
    private void loadRecurringExpenses(int userId) {
        Models = repository.getListRecurring(userId);
    }
    private void setupRecyclerView() {
        budget = new Recurring_Adapter(Models, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RCC.setLayoutManager(manager);
        RCC.setAdapter(budget);
    }
    private void setupCreateButtonListener() {
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecurringActivity.class);
            startActivity(intent);
        });
    }
    private void setupItemClickListener(int userId) {
        budget.setOnClickListener(position -> {
            Expense_Recurring_Model selectedModel = Models.get(position);
            Intent intent = new Intent(getActivity(), EditRecurringActivity.class);
            Bundle bundle = new Bundle();
            // Put data into bundle, format dates as yyyy-MM-dd for safety
            bundle.putInt("ID_RECURRING", selectedModel.getId());
            bundle.putString("NAME_RECURRING", selectedModel.getName());
            bundle.putDouble("MONEY_RECURRING", selectedModel.getExpense());
            bundle.putString("NOTE_RECURRING", selectedModel.getNote());
            bundle.putString("START_DATE", formatLocalDateTime(selectedModel.getStart_date()));
            bundle.putString("END_DATE", formatLocalDateTime(selectedModel.getEnd_date()));
            bundle.putInt("REPEAT_DAYS", selectedModel.getRepeatInterval());
            // bundle.putInt("CATEGORY_ID", selectedModel.getCategoryId()); // Removed as per your note
            bundle.putString("CATEGORY_NAME", selectedModel.getCategoryName());
            bundle.putInt("USER_ID", userId);

            intent.putExtras(bundle);
            startActivity(intent);
        });
        budget.setOnDeleteListener(position -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        Expense_Recurring_Model item = Models.get(position);
                        long res = repository.deleteRecurringById(item.getId());
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
    // Helper method to format LocalDateTime to "yyyy-MM-dd" string
    private String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.toLocalDate().toString(); // Returns yyyy-MM-dd
        }
        return "";
    }
    @Override
    public void onResume() {
        super.onResume();
        int userId = loadUserId();  // Lấy lại userId
        Models = repository.getListRecurring(userId);  // Load lại dữ liệu mới từ DB
        if (budget != null) {
            budget.updateData(Models);  // Cập nhật dữ liệu cho adapter và refresh UI
        }
    }
}