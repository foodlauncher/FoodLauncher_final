package com.launcher.foodlauncher.ui.find_food;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.launcher.foodlauncher.R;
import com.launcher.foodlauncher.api.ApiRandomSearchInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomSearchFragment extends Fragment {

    private Context context;
    private TextView rSelectedDiet, rSelectedMealType, rSelectedCuisine, textViewResult;

    private Button cuisineBtn, dietBtn, mealBtn;
    private ImageButton btnFind;

    private String meal, cuisine, diet;
    RecyclerView recyclerCuisine, recyclerDiet, recyclerMeal, recycleFood;

    BottomSheetBehavior foodBehaviour;

    private final String apiKey = "2bd5395fcb6441b3a37139d3b0fab4eb";

    public RandomSearchFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_random_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cuisineBtn = getView().findViewById(R.id.cuisine_btn);
        dietBtn = getView().findViewById(R.id.diet_btn);
        mealBtn = getView().findViewById(R.id.meal_btn);
        btnFind = getView().findViewById(R.id.btn_find);

        rSelectedCuisine = getView().findViewById(R.id.selected_cuisine);
        rSelectedDiet = getView().findViewById(R.id.selected_diet);
        rSelectedMealType = getView().findViewById(R.id.selected_meal);

        BottomSheetBehavior mealBehaviour, dietBehaviour, cuisineBehaviour, intBehaviour;
        FrameLayout mealBottom = getView().findViewById(R.id.bottom_input_meal);
        mealBehaviour = BottomSheetBehavior.from(mealBottom);
        FrameLayout cuisineBottom = getView().findViewById(R.id.bottom_input_cuisine);
        cuisineBehaviour = BottomSheetBehavior.from(cuisineBottom);
        FrameLayout dietBottom = getView().findViewById(R.id.bottom_input_diet);
        dietBehaviour = BottomSheetBehavior.from(dietBottom);
        FrameLayout foodBottom = getView().findViewById(R.id.bottom_food_sheet);
        foodBehaviour = BottomSheetBehavior.from(foodBottom);

        String[] cuisineArray = getResources().getStringArray(R.array.cuisines);
        String[] dietArray = getResources().getStringArray(R.array.diets);
        String[] mealArray = getResources().getStringArray(R.array.meal_types);

        recyclerMeal = getView().findViewById(R.id.recycle_meal);
        recyclerMeal.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerDiet = getView().findViewById(R.id.recycle_diet);
        recyclerDiet.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCuisine = getView().findViewById(R.id.recycle_cuisine);
        recyclerCuisine.setLayoutManager(new LinearLayoutManager(getContext()));

        ListAdapterRadio mealAdapter = new ListAdapterRadio(mealArray, R.layout.radiobutton, getContext());
        ListAdapterRadio dietAdapter = new ListAdapterRadio(dietArray, R.layout.radiobutton, getContext());
        ListAdapterCheck cuisineAdapter = new ListAdapterCheck(cuisineArray, R.layout.checkbox, getContext());

        recyclerCuisine.setAdapter(cuisineAdapter);
        recyclerDiet.setAdapter(dietAdapter);
        recyclerMeal.setAdapter(mealAdapter);

        mealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                dietBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        dietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dietBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                mealBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        cuisineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                mealBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                dietBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cuisine = cuisineAdapter.getCheckedValue();
                    diet = dietAdapter.getCheckedValue();
                    meal = mealAdapter.getCheckedValue();

                    rSelectedMealType.setVisibility(View.VISIBLE);
                    rSelectedDiet.setVisibility(View.VISIBLE);
                    rSelectedCuisine.setVisibility(View.VISIBLE);

                    rSelectedCuisine.setText(cuisine);
                    rSelectedDiet.setText(diet);
                    rSelectedMealType.setText(meal);

                    searchRandomFood(cuisine, diet, meal);

                } catch (Exception e) {
                    Log.i("TAG", "onClick: EXCEPTION" + e.getMessage());
                }
            }
        });
    }

    private void searchRandomFood (String cuisine, String diet, String meal) {

        StringBuilder tags = new StringBuilder();
        tags.append(diet).append(",").append(meal).append(",").append(cuisine);
        textViewResult = getView().findViewById(R.id.text_view_result);

        Log.d("TAG", "searchRandomFood: " + cuisine + " " + diet + " " + meal);

        recycleFood = (RecyclerView) getView().findViewById(R.id.recycle_food);
        recycleFood.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRandomSearchInterface apiRandomSearchInterface = retrofit.create(ApiRandomSearchInterface.class);

        try {
        Call<RandomSearch> call = apiRandomSearchInterface.getRandomSearchResult(apiKey, 30, tags.toString().toLowerCase());

        call.enqueue(new Callback<RandomSearch>() {
            @Override
            public void onResponse(Call<RandomSearch> call, Response<RandomSearch> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText("Response code: " + response.code() + call.request().toString());
                    return;
                }
                RandomSearch randomSearch = response.body();
                Log.i("TAG", "onResponse: url" + response.toString());

                List<Recipe> recipes = randomSearch.getRecipes();

                recycleFood.setAdapter(new FoodAdapter(recipes, R.layout.food_adapter, getContext()));

                foodBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }

            @Override
            public void onFailure(Call<RandomSearch> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
        } catch (Exception e) {
            Log.i("TAG", "onClick: EXCEPTION  : " + e.getMessage());
        }
    }
}