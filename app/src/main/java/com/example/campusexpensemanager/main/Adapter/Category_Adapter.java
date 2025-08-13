package com.example.campusexpensemanager.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;

import java.util.ArrayList;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.CategoryItemViewHolder> {
    public ArrayList<Category_Expense_Model> budgetModels;
    public Context context;
    public OnClickListener onClickListener;
    public OnDeleteListener onDeleteListener;  // thêm callback xóa

    // Giao diện callback
    public interface OnClickListener {
        void onClick(int position);
    }

    // Giao diện callback cho xóa
    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public void setOnDeleteListener(OnDeleteListener deleteListener) {
        this.onDeleteListener = deleteListener;
    }

    public Category_Adapter(ArrayList<Category_Expense_Model> model, Context context) {
        this.budgetModels = model;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_expense_view, parent, false);
        return new CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        Category_Expense_Model model = budgetModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvMoney.setText(String.valueOf(model.getBudget()));
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;
        ImageButton btnDelete;  // nút xóa

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBudget);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            btnDelete = itemView.findViewById(R.id.btnDelete);  // lấy nút xóa

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(getAdapterPosition());
                }
            });
        }
    }
}
