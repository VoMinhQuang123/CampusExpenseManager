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

import java.util.ArrayList;

public class Overview_Adapter extends RecyclerView.Adapter<Overview_Adapter.OverViewItemViewHolder>{
    public ArrayList<Category_Expense_Model> budgetModels;
    public Context context;
    public Overview_Adapter.OnClickListener onClickListener;

    // Giao diện callback
    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(Overview_Adapter.OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public void setData(ArrayList<Category_Expense_Model> newList) {
        this.budgetModels = newList;
        notifyDataSetChanged();
    }

    public Overview_Adapter(ArrayList<Category_Expense_Model> model, Context context) {
        this.budgetModels = model;
        this.context = context;
    }

    @NonNull
    @Override
    public Overview_Adapter.OverViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_expense_view, parent, false);
        return new Overview_Adapter.OverViewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Overview_Adapter.OverViewItemViewHolder holder, int position) {
        Category_Expense_Model model = budgetModels.get(position);
        holder.tvName.setText(model.getName());
        holder.tvMoney.setText(String.valueOf(model.getBudget()));
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public class OverViewItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney;

        public OverViewItemViewHolder(@NonNull View itemView) {
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
