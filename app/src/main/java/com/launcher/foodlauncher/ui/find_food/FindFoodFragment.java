package com.launcher.foodlauncher.ui.find_food;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.launcher.foodlauncher.R;

import java.util.ArrayList;

public class FindFoodFragment extends Fragment {

    private FindFoodViewModel findFoodViewModel;

    private Button searchFood, searchRandom;
    private RelativeLayout relChoice, relFood, relRandom;



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

        searchFood = getView().findViewById(R.id.search_btn);
        searchRandom = getView().findViewById(R.id.random_btn);

        searchFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFood fragment = new SearchFood(getContext());
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        searchRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomSearchFragment fragment = new RandomSearchFragment(getContext());
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
