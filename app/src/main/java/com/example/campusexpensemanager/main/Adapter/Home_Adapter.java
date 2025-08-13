package com.example.campusexpensemanager.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.main.Model.CategoryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.HomeItemViewHolder> {

    private Context context;
    private List<CategoryData> categoryList;

    public Home_Adapter(Context context, List<CategoryData> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_view, parent, false);
        return new HomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder holder, int position) {
        CategoryData category = categoryList.get(position);

        holder.tvCategoryName.setText(category.getName());
        holder.tvTotalSpent.setText(formatMoney(category.getTotalSpent()));
        holder.tvCategoryTotal.setText(formatMoney(category.getTotalCategory()));
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public static class HomeItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvTotalSpent, tvCategoryTotal;

        public HomeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvTotalSpent = itemView.findViewById(R.id.tvTotalSpent);
            tvCategoryTotal = itemView.findViewById(R.id.tvCategoryTotal);
        }
    }

    private String formatMoney(int amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }
}
