package com.example.kpa;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_history extends Fragment {
    private Button btnShowList, btnShowCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        btnShowList = view.findViewById(R.id.btnShowList);
        btnShowCalendar = view.findViewById(R.id.btnShowCalendar);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.history_fragment_container, new fragment_history_list())
                    .commit();
        }

        btnShowList.setOnClickListener(v -> switchFragment(new fragment_history_list()));
        btnShowCalendar.setOnClickListener(v -> switchFragment(new fragment_history_calendar())); // ✅ FIXED THIS LINE

        return view;
    }

    private void switchFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.history_fragment_container, fragment)
                .commit();
    }
}
