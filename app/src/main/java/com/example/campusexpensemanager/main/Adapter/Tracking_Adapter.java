package com.example.campusexpensemanager.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.Expense_Recurring_Model;
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;

import java.util.ArrayList;

public class Tracking_Adapter extends RecyclerView.Adapter<Tracking_Adapter.TrackingItemViewHolder> {
    public ArrayList<Expense_Tracking_Model> budgetModels;
    public Context context;
    public OnClickListener onClickListener;

    // Giao diện callback
    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public Tracking_Adapter(ArrayList<Expense_Tracking_Model> model, Context context) {
        this.budgetModels = model;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tracking_view, parent, false);
        return new TrackingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingItemViewHolder holder, int position) {
        Expense_Tracking_Model model = budgetModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvMoney.setText(String.valueOf(model.getExpense()));
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public class TrackingItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;

        public TrackingItemViewHolder(@NonNull View itemView) {
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
