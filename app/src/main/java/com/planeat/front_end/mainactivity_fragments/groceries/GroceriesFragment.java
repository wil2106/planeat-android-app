package com.planeat.front_end.mainactivity_fragments.groceries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.planeat.front_end.R;

public class GroceriesFragment extends Fragment {

    private GroceriesViewModel groceriesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groceriesViewModel =
                ViewModelProviders.of(this).get(GroceriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_groceries, container, false);
        final TextView textView = root.findViewById(R.id.text_groceries);
        groceriesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}