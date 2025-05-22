package com.example.kpa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryShiftAdapter extends RecyclerView.Adapter<HistoryShiftAdapter.ShiftItemViewHolder> {

    private Context context;
    private List<ShiftEntity> shiftHistoryList;
    private OnShiftClickListener shiftClickListener;

    public HistoryShiftAdapter(Context context, List<ShiftEntity> shiftHistoryList, OnShiftClickListener shiftClickListener) {
        this.context = context;
        this.shiftHistoryList = shiftHistoryList;
        this.shiftClickListener = shiftClickListener;
    }

    @Override
    public ShiftItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shift_overview_layout, parent, false);
        return new ShiftItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShiftItemViewHolder holder, int position) {
        ShiftEntity shiftEntity = shiftHistoryList.get(position);
        holder.bind(shiftEntity);
    }

    @Override
    public int getItemCount() {
        return shiftHistoryList.size();
    }

    public class ShiftItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShiftDate;

        public ShiftItemViewHolder(View itemView) {
            super(itemView);

            tvShiftDate = itemView.findViewById(R.id.tvShiftDate);
        }

        public void bind(final ShiftEntity shiftEntity) {

            tvShiftDate.setText(shiftEntity.getShiftDate());


            itemView.setOnClickListener(v -> {

                FragmentWorkDetails fragment = FragmentWorkDetails.newInstance(shiftEntity);

                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }


    public interface OnShiftClickListener {
        void onShiftClick(String shiftDate);
    }
}
