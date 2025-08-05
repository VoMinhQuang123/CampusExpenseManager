package com.example.campusexpensemanager.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.Category_Expense_Model;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;

import java.util.ArrayList;

public class Recurring_Adapter extends RecyclerView.Adapter<Recurring_Adapter.RecurringItemViewHolder> {
    public ArrayList<Expense_Recurring_Model> budgetModels;
    public Context context;
    public OnClickListener onClickListener;

    // Giao diện callback
    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
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
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public class RecurringItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;

        public RecurringItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBudget);
            tvMoney = itemView.findViewById(R.id.tvMoney);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
