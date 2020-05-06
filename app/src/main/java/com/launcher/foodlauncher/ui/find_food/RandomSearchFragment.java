package com.launcher.foodlauncher.ui.find_food;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.launcher.foodlauncher.R;

import java.util.ArrayList;
import java.util.List;

public class RandomSearchFragment extends Fragment {

    private Context context;
    private TextView rSelectedDiet, rSelectedMealType, rSelectedCuisine, rSelectedIntolerances;
    private StringBuilder rSelDiet, rSelMeal, rSelCuisine, rSelInt;

    private Button cuisineBtn, dietBtn, intolerancesBtn, mealBtn;
    private ImageButton btnFind;
    View dialogView;

    public RandomSearchFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dialogView = LayoutInflater.from(getContext()).inflate(R.layout.inputs, container, false);
        return inflater.inflate(R.layout.fragment_random_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cuisineBtn = getView().findViewById(R.id.cuisine_btn);
        dietBtn = getView().findViewById(R.id.diet_btn);
        intolerancesBtn = getView().findViewById(R.id.intolerances_btn);
        mealBtn = getView().findViewById(R.id.meal_btn);
        btnFind = getView().findViewById(R.id.btn_find);

        rSelectedCuisine = getView().findViewById(R.id.selected_cuisine);

        String[] cuisineArray = getResources().getStringArray(R.array.cuisines);
        String[] dietArray = getResources().getStringArray(R.array.diets);
        String[] intolerancesArray = getResources().getStringArray(R.array.intolerances);
        String[] mealArray = getResources().getStringArray(R.array.meal_types);

        androidx.appcompat.app.AlertDialog.Builder cuisineBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);


        LinearLayout lCuisine = dialogView.findViewById(R.id.main_ll);
        LinearLayout.LayoutParams paramsCuisine = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        SparseBooleanArray checkedCuisine = new SparseBooleanArray();
        for (int i = 0; i < cuisineArray.length; i++) {
            LinearLayout item = new LinearLayout(getContext());
            item.setLayoutParams(paramsCuisine);
            item.setOrientation(LinearLayout.VERTICAL);
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(cuisineArray[i]);
            item.addView(checkBox);
            lCuisine.addView(item);
            int finalI = i;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkedCuisine.get(finalI, false)){
                        ((CheckBox) checkBox).setChecked(false);
                        checkedCuisine.delete(finalI);
                    } else {
                        ((CheckBox) checkBox).setChecked(true);
                        checkedCuisine.put(finalI, true);
                    }
                }
            });
        }

        for(int i=0; i<cuisineArray.length; i++) {
            rSelCuisine.append(cuisineArray[checkedCuisine.keyAt(i)]);
        }

        cuisineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineBuilder.setView(lCuisine);
                AlertDialog alertDialog = cuisineBuilder.create();
                alertDialog.show();
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rSelectedCuisine.setText(rSelCuisine.toString());
                rSelectedCuisine.setVisibility(View.VISIBLE);
            }
        });

    }
}