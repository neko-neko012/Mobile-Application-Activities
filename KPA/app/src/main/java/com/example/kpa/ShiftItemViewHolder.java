package com.example.kpa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

public class ShiftItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvShiftDate;
    public CardView cardView;

    public ShiftItemViewHolder(View itemView) {
        super(itemView);

        // Initialize the views
        tvShiftDate = itemView.findViewById(R.id.tvShiftDate);
        cardView = itemView.findViewById(R.id.card_view_shift);
    }


    public void bind(ShiftEntity shiftEntity) {

        tvShiftDate.setText(shiftEntity.getShiftDate());


        cardView.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putParcelable("shiftDetails", shiftEntity);



            FragmentWorkDetails fragment = new FragmentWorkDetails();
            fragment.setArguments(bundle);


            FragmentTransaction transaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    // Static method to create the ViewHolder from XML layout
    public static ShiftItemViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shift_history, parent, false);
        return new ShiftItemViewHolder(view);
    }
}
