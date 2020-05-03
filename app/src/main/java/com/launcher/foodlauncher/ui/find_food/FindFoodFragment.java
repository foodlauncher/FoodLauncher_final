package com.launcher.foodlauncher.ui.find_food;

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

import com.launcher.foodlauncher.R;

public class FindFoodFragment extends Fragment {

    private FindFoodViewModel findFoodViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findFoodViewModel =
                ViewModelProviders.of(this).get(FindFoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_find_food, container, false);
        final TextView textView = root.findViewById(R.id.text_find_food);
        findFoodViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
