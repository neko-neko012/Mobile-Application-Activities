package com.example.sipaglang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder> {
    private List<WorkEntity> workList;
    private final Context context;
    private final WorkDatabase workDatabase;

    public WorkAdapter(Context context, List<WorkEntity> workList) {
        this.context = context;
        this.workDatabase = WorkDatabase.getInstance(context);
        this.workList = workList != null ? workList : new ArrayList<>();
    }

    public void setWorkList(List<WorkEntity> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new WorkDiffCallback(workList, newList));
        workList.clear();
        workList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_work, parent, false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        WorkEntity work = workList.get(position);
        holder.tvWorkType.setText(work.getWorkType());
        holder.tvQuantity.setText("Quantity: " + work.getQuantity());
        holder.tvPrice.setText("Price: ₱" + work.getPrice());

        double totalCost = work.getPrice() * work.getQuantity();
        holder.tvTotal.setText(String.format(Locale.getDefault(), "Total: ₱%.2f", totalCost));

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WorkDetailsActivity.class);
            intent.putExtra("workId", work.getId());
            intent.putExtra("workType", work.getWorkType());
            intent.putExtra("quantity", work.getQuantity());
            intent.putExtra("price", work.getPrice());
            intent.putExtra("imagePath", work.getImageUri());
            intent.putExtra("workId", work.getId());

            context.startActivity(intent);
        });

        holder.cardView.setOnLongClickListener(v -> {
            // Show a confirmation dialog to delete the work entry
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this item?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, id) -> removeItem(position))
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                    .create()
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

    static class WorkViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkType, tvQuantity, tvPrice, tvTotal;
        CardView cardView;

        WorkViewHolder(View itemView) {
            super(itemView);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTotal = itemView.findViewById(R.id.tv_total);
            cardView = itemView.findViewById(R.id.card_view_work);
        }
    }


    public void removeItem(int position) {
        WorkEntity deletedWork = workList.get(position);
        WorkDatabase.getDatabaseExecutor().execute(() -> {
            workDatabase.workDao().deleteById(deletedWork.getId());
            workList.remove(position);
            ((android.app.Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, workList.size());
            });
        });
    }


    public double getTotalEarnings() {
        double total = 0;
        for (WorkEntity work : workList) {
            total += work.getPrice() * work.getQuantity();
        }
        return total;
    }

    public List<WorkEntity> getWorkList() {
        return workList;
    }
}
