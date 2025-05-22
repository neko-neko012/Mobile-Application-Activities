    package com.example.kpa;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.recyclerview.widget.RecyclerView;
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.Observer;

    import java.util.ArrayList;
    import java.util.List;

    public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder> {

        private List<ShiftEntity> shiftList;
        private OnItemClickListener onItemClickListener;

        public ShiftAdapter() {
            shiftList = new ArrayList<>();
        }

        @Override
        public ShiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_overview_card, parent, false);
            return new ShiftViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ShiftViewHolder holder, int position) {
            ShiftEntity shift = shiftList.get(position);
            holder.bind(shift);
        }

        @Override
        public int getItemCount() {
            return shiftList.size();
        }

        public void setShifts(List<ShiftEntity> shifts) {
            this.shiftList = shifts;
            notifyDataSetChanged();
        }

        public class ShiftViewHolder extends RecyclerView.ViewHolder {
            private TextView tvShiftDate;

            public ShiftViewHolder(View itemView) {
                super(itemView);
                tvShiftDate = itemView.findViewById(R.id.tvShiftDate);

                itemView.setOnClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(shiftList.get(getAdapterPosition()));
                    }
                });
            }

            public void bind(ShiftEntity shift) {
                tvShiftDate.setText(shift.getShiftDate());
            }
        }

        public interface OnItemClickListener {
            void onItemClick(ShiftEntity shift);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }
    }
