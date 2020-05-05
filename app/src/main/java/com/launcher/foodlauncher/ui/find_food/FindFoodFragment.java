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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.launcher.foodlauncher.R;

import java.util.ArrayList;

public class FindFoodFragment extends Fragment {

    private FindFoodViewModel findFoodViewModel;

    private Button searchFood, searchRandom;
    private RelativeLayout relChoice, relFood, relRandom;

    private Spinner rIntolerances, rMealType, rDiet, rCuisine;

    private TextView rSelectedDiet, rSelectedMealType, rSelectedCuisine, rSelectedIntolerances;

    private String rSelDiet, rSelMeal, rSelCuisine, rSelInt;

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
        relChoice = getView().findViewById(R.id.choice);
        relFood = getView().findViewById(R.id.specific);
        relRandom = getView().findViewById(R.id.random);

        searchFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relChoice.setVisibility(View.GONE);
                relFood.setVisibility(View.VISIBLE);
            }
        });

        searchRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relChoice.setVisibility(View.GONE);
                relRandom.setVisibility(View.VISIBLE);
            }
        });

        rCuisine = (Spinner) getView().findViewById(R.id.cuisines);
        rMealType = (Spinner) getView().findViewById(R.id.meal_type);
        rDiet = (Spinner) getView().findViewById(R.id.diet);
        rIntolerances = (Spinner) getView().findViewById(R.id.intolerances);

        rSelectedCuisine = getView().findViewById(R.id.selected_cuisine);
        rSelectedDiet = getView().findViewById(R.id.selected_diet);
        rSelectedIntolerances = getView().findViewById(R.id.selected_intolerances);
        rSelectedMealType = getView().findViewById(R.id.selected_meal_type);

        String[] intolerancesArray = getResources().getStringArray(R.array.intolerances);
        String[] mealTypesArray = getResources().getStringArray(R.array.meal_types);
        String[] dietsArray = getResources().getStringArray(R.array.diets);
        String[] cuisinesArray = getResources().getStringArray(R.array.cuisines);

        SparseArray<String> intolerancesSparse = new SparseArray<>();
        SparseArray<String> mealSparse = new SparseArray<>();
        SparseArray<String> dietSparse = new SparseArray<>();
        SparseArray<String> cuisineSparse = new SparseArray<>();

        for(int i = 0; i<intolerancesArray.length; i++) {
            intolerancesSparse.append(i, intolerancesArray[i]);
        }

        for(int i = 0; i<mealTypesArray.length; i++) {
            mealSparse.append(i, mealTypesArray[i]);
        }

        for(int i = 0; i<dietsArray.length; i++) {
            dietSparse.append(i, dietsArray[i]);
        }

        for(int i = 0; i<cuisinesArray.length; i++) {
            cuisineSparse.append(i, cuisinesArray[i]);
        }

        SpinnerAdapter rCuisineAdapter = new SpinnerAdapter(getContext(), cuisineSparse, "Cuisines");
        SpinnerAdapter rIntolerancesAdapter = new SpinnerAdapter(getContext(), intolerancesSparse, "Intolerances");
        ArrayAdapter rDietAdapter = ArrayAdapter.createFromResource(getContext(), R.array.diets, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter rMealAdapter = ArrayAdapter.createFromResource(getContext(), R.array.meal_types, R.layout.support_simple_spinner_dropdown_item);

        rCuisine.setAdapter(rCuisineAdapter);
        rIntolerances.setAdapter(rIntolerancesAdapter);
        rDiet.setAdapter(rDietAdapter);
        rMealType.setAdapter(rMealAdapter);

        rMealType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rSelMeal = mealTypesArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rSelDiet = dietsArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rSelCuisine = rCuisineAdapter.getCheckedValues();
//        rSelDiet = rDietAdapter.getRadioChecked();
//        rSelMeal = rMealAdapter.getRadioChecked();
        rSelInt = rIntolerancesAdapter.getCheckedValues();

        rSelectedMealType.setVisibility(View.VISIBLE);
        rSelectedMealType.setText(rSelMeal);

        rSelectedCuisine.setVisibility(View.VISIBLE);
        rSelectedCuisine.setText(rSelCuisine);

        rSelectedIntolerances.setVisibility(View.VISIBLE);
        rSelectedIntolerances.setText(rSelInt);

        rSelectedDiet.setVisibility(View.VISIBLE);
        rSelectedDiet.setText(rSelDiet);

    }

    @Override
    public void onResume() {
        super.onResume();
        relChoice.setVisibility(View.VISIBLE);
        relRandom.setVisibility(View.GONE);
    }
}
