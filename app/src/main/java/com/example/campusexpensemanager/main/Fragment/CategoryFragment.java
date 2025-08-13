package com.example.campusexpensemanager.main.Fragment;

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Category.EditCategoryActivity;
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
    private Button btnCreateBudget;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        // Ánh xạ các view
        btnCreateBudget = view.findViewById(R.id.btnbudgetCeater);
        budgetRCC = view.findViewById(R.id.rvBudget);
        ViewExpense = view.findViewById(R.id.viewExpense);

        // Khởi tạo repository và lấy danh sách budget theo userId
        repository = new Category_Expense_Repository(getActivity());
        budgetModels = repository.getListBudget(userId);

        // Khởi tạo adapter và gán cho RecyclerView
        budget = new Category_Adapter(budgetModels, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetRCC.setLayoutManager(manager);
        budgetRCC.setAdapter(budget);

        // Hiển thị tổng số tiền
        int total = repository.getTotalExpense(userId);
        ViewExpense.setText("Amounts: " + total + " VNĐ");

        // Xử lý sự kiện click vào item để sửa
        budget.setOnClickListener(position -> {
            Category_Expense_Model model1 = budgetModels.get(position);
            Intent intent = new Intent(getActivity(), EditCategoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID_BUDGET", model1.getId());
            bundle.putString("NAME_BUDGET", model1.getName());
            bundle.putInt("MONEY_BUDGET", model1.getBudget());
            bundle.putString("DESCRIPTION", model1.getDescription());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        budget.setOnDeleteListener(position -> {
            Category_Expense_Model itemToDelete = budgetModels.get(position);
            int idToDelete = itemToDelete.getId();

            if (idToDelete == 1) {
                Toast.makeText(getContext(), "Category 'Other' không được phép xóa", Toast.LENGTH_SHORT).show();
                return; // không cho phép xóa
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        long result = repository.deleteBudget(idToDelete);

                        if (result > 0) {
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            budgetModels.remove(position);
                            budget.notifyItemRemoved(position);

                            // Cập nhật lại các bản ghi tracking & recurring gán về category 'Other'
                            repository.updateTrackingCategoryToOther(idToDelete);
                            repository.updateRecurringCategoryToOther(idToDelete);

                            // Cập nhật tổng tiền sau khi xóa
                            int newTotal = repository.getTotalExpense(userId);
                            ViewExpense.setText("Amounts: " + newTotal + " VNĐ");
                        } else {
                            Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        // Nút tạo budget mới
        btnCreateBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);

        budgetModels.clear();
        budgetModels.addAll(repository.getListBudget(userId));
        budget.notifyDataSetChanged();

        int total = repository.getTotalExpense(userId);
        ViewExpense.setText("Amounts: " + total + " VNĐ");
    }



}