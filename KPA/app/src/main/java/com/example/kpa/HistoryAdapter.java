package com.example.kpa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private List<WorkEntity> historyList;

    public HistoryAdapter(Context context, List<WorkEntity> historyList) {
        this.context = context;
        this.historyList = historyList;
    }


    public void setHistoryList(List<WorkEntity> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_work, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WorkEntity work = historyList.get(position);

        holder.tvWorkType.setText(work.getWorkType());
        holder.tvQuantity.setText("Quantity: " + work.getQuantity());
        holder.tvPrice.setText("Price: ₱" + work.getPrice());


        holder.tvTotal.setText("Total: ₱" + (work.getQuantity() * work.getPrice()));
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }


    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkType, tvQuantity, tvPrice, tvLocation, tvTotal;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }
}
