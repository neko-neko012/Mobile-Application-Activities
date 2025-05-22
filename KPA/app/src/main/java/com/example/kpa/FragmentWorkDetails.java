package com.example.kpa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
public class FragmentWorkDetails extends Fragment {

    private TextView historyWorkType, historyQuantity, historyPrice, historyTotal;
    private ImageView historyImgWork;
    private Button historyBtnDeleteWork;

    private ShiftViewModel shiftViewModel;
    private static final String ARG_SHIFT = "shift_entity";

    private ShiftEntity shiftEntity;
    public static FragmentWorkDetails newInstance(ShiftEntity shiftEntity) {
        FragmentWorkDetails fragment = new FragmentWorkDetails();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SHIFT, shiftEntity);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail_list, container, false);
        historyWorkType = view.findViewById(R.id.history_work_type);
        historyQuantity = view.findViewById(R.id.history_quantity);
        historyPrice = view.findViewById(R.id.history_price);
        historyTotal = view.findViewById(R.id.history_total);
        historyImgWork = view.findViewById(R.id.history_img_work);
        historyBtnDeleteWork = view.findViewById(R.id.history_btn_delete_work);


        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftViewModel.class);

        // Get the work arguments
        if (getArguments() != null) {
            shiftEntity = (ShiftEntity) getArguments().getSerializable("shiftDetails");
            if (shiftEntity != null) {
                displayWorkDetails(shiftEntity);
            }
        }

        historyBtnDeleteWork.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void displayWorkDetails(ShiftEntity shift) {
        // Display the details of the work entry
        historyWorkType.setText(shift.getWorkType());
        historyQuantity.setText("Quantity: " + shift.getQuantity());
        historyPrice.setText("Price: " + shift.getPrice());
        historyTotal.setText("Total: " + (shift.getQuantity() * shift.getPrice()));

        // Load the image if it's available (using Glide for image loading)
        if (shift.getImageUri() != null && !shift.getImageUri().isEmpty()) {
            Glide.with(requireContext())
                    .load(shift.getImageUri())
                    .into(historyImgWork);
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete this work entry?")
                .setPositiveButton("Yes", (dialog, which) -> deleteWorkEntry())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteWorkEntry() {
        // Delete the work entry from the database
        if (shiftEntity != null) {
            shiftViewModel.delete(shiftEntity);

            getActivity().onBackPressed();
        }
    }
}
