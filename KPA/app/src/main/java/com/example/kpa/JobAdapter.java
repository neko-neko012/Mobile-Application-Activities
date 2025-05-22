package com.example.kpa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kpa.Job;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the job card layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_overview_card, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }


    public class JobViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWorkType;
        private TextView tvQuantity;
        private TextView tvPrice;
        private TextView tvLocation;
        private TextView tvTotal;

        public JobViewHolder(View itemView) {
            super(itemView);
            tvWorkType = itemView.findViewById(R.id.tv_work_type);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }

        public void bind(Job job) {
            tvWorkType.setText(job.getJobName());
            tvQuantity.setText(String.valueOf(job.getQuantity()));
            tvPrice.setText(String.format("$%.2f", job.getPrice()));
            tvLocation.setText(job.getLocation()); // Display location
            tvTotal.setText(String.format("Total: $%.2f", job.getTotal()));
        }
    }
}
