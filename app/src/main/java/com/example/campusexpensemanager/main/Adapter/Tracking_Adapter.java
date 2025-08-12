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
import com.example.campusexpensemanager.main.Model.Expense_Tracking_Model;

import java.util.ArrayList;

public class Tracking_Adapter extends RecyclerView.Adapter<Tracking_Adapter.TrackingItemViewHolder> {
    private ArrayList<Expense_Tracking_Model> budgetModels;
    private Context context;

    private OnClickListener onClickListener;
    private OnDeleteListener onDeleteListener;

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

    public class TrackingItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;
        ImageButton btnDelete;

        public TrackingItemViewHolder(@NonNull View itemView) {
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

    public void updateData(ArrayList<Expense_Tracking_Model> newModels) {
        budgetModels.clear();
        budgetModels.addAll(newModels);
        notifyDataSetChanged();
    }
}
