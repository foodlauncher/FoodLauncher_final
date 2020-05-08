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
import com.launcher.foodlauncher.ui.home.HomeFragment;

import java.util.ArrayList;

public class FindFoodFragment extends Fragment {

    private FindFoodViewModel findFoodViewModel;

    private int fragSelected;
    String fragKey = "selected_frag";

    private Button searchFood, searchRandom;

    private String tagHome = "HomeFragmentTag", tagFindFood = "FindFoodFragmentTag",
            tagFavourite = "FavouritesFragmentTag", tagUser = "UserFragmentTag",
            tagSearch = "SearchFragmentTag", tagRandom = "RandomSearchFragmentTag";

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

        if(savedInstanceState != null) {
            fragSelected = savedInstanceState.getInt(fragKey);
        }

        if(fragSelected == 1) {
            SearchFood fragment = (SearchFood)
                    getChildFragmentManager().findFragmentByTag(tagSearch);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, tagSearch);
            transaction.commit();
        }

        if(fragSelected == 2) {
            RandomSearchFragment fragment = (RandomSearchFragment)
                    getChildFragmentManager().findFragmentByTag(tagRandom);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, tagRandom);
            transaction.commit();
        }

        searchFood = getView().findViewById(R.id.search_btn);
        searchRandom = getView().findViewById(R.id.random_btn);

        searchFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragSelected = 1;
                SearchFood fragment = new SearchFood(getContext());
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment, tagSearch);
                transaction.commit();
            }
        });

        searchRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragSelected = 2;
                RandomSearchFragment fragment = new RandomSearchFragment(getContext());
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment, tagRandom);
                transaction.commit();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(fragKey, fragSelected);
        super.onSaveInstanceState(outState);
    }
}
