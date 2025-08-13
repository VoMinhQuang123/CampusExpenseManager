package com.example.campusexpensemanager.main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Activity.Recurring.EditRecurringActivity;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;

import java.util.ArrayList;

public class Recurring_Adapter extends RecyclerView.Adapter<Recurring_Adapter.RecurringItemViewHolder> {
    public ArrayList<Expense_Recurring_Model> budgetModels;
    public Context context;

    public OnClickListener onClickListener;
    public OnDeleteListener onDeleteListener;

    // Giao diện callback click item
    public interface OnClickListener {
        void onClick(int position);
    }

    // Giao diện callback xóa item
    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public void setOnDeleteListener(OnDeleteListener deleteListener) {
        this.onDeleteListener = deleteListener;
    }

    public Recurring_Adapter(ArrayList<Expense_Recurring_Model> model, Context context) {
        this.budgetModels = model;
        this.context = context;
    }

    @NonNull
    @Override
    public RecurringItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recurring_view, parent, false);
        return new RecurringItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecurringItemViewHolder holder, int position) {
        Expense_Recurring_Model model = budgetModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvMoney.setText(String.valueOf(model.getExpense()));

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteListener != null) {
                onDeleteListener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public class RecurringItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;
        ImageButton btnDelete;

        public RecurringItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBudget);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
    public void updateData(ArrayList<Expense_Recurring_Model> newModels) {
        budgetModels.clear();
        budgetModels.addAll(newModels);
        notifyDataSetChanged();
    }
}
