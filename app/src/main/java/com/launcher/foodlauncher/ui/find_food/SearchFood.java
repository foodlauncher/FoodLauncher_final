package com.launcher.foodlauncher.ui.find_food;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.launcher.foodlauncher.R;
import com.launcher.foodlauncher.api.ApiComplexSearchInterface;
import com.launcher.foodlauncher.api.ApiRandomSearchInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFood extends Fragment {

    private Context context;
    private TextView textViewResult;

    private Button cuisineBtn, dietBtn, intolerancesBtn, mealBtn;
    private ImageButton btnFind;

    private EditText query, include, exclude, maxFat, maxCalories;
    private String meal, cuisine, diet, intolerances;
    RecyclerView recyclerCuisine, recyclerDiet, recyclerMeal, recyclerInt, recycleFood;

    BottomSheetBehavior foodBehaviour;

    private final String apiKey = "2bd5395fcb6441b3a37139d3b0fab4eb";

    public SearchFood(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_food, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cuisineBtn = getView().findViewById(R.id.cuisine_btn);
        dietBtn = getView().findViewById(R.id.diet_btn);
        intolerancesBtn = getView().findViewById(R.id.intolerances_btn);
        mealBtn = getView().findViewById(R.id.meal_btn);
        btnFind = getView().findViewById(R.id.btn_find);

        query = getView().findViewById(R.id.query);
        maxCalories = getView().findViewById(R.id.max_calories);
        maxFat = getView().findViewById(R.id.max_fat);
        include = getView().findViewById(R.id.include);
        exclude = getView().findViewById(R.id.exclude);

        BottomSheetBehavior mealBehaviour, dietBehaviour, cuisineBehaviour, intBehaviour;
        FrameLayout mealBottom = getView().findViewById(R.id.bottom_input_meal);
        mealBehaviour = BottomSheetBehavior.from(mealBottom);
        FrameLayout intBottom = getView().findViewById(R.id.bottom_input_intolerances);
        intBehaviour = BottomSheetBehavior.from(intBottom);
        FrameLayout cuisineBottom = getView().findViewById(R.id.bottom_input_cuisine);
        cuisineBehaviour = BottomSheetBehavior.from(cuisineBottom);
        FrameLayout dietBottom = getView().findViewById(R.id.bottom_input_diet);
        dietBehaviour = BottomSheetBehavior.from(dietBottom);
        FrameLayout foodBottom = getView().findViewById(R.id.bottom_complex_sheet);
        foodBehaviour = BottomSheetBehavior.from(foodBottom);

        String[] cuisineArray = getResources().getStringArray(R.array.cuisines);
        String[] dietArray = getResources().getStringArray(R.array.diets);
        String[] intolerancesArray = getResources().getStringArray(R.array.intolerances);
        String[] mealArray = getResources().getStringArray(R.array.meal_types);

        recyclerMeal = getView().findViewById(R.id.recycle_meal);
        recyclerMeal.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerDiet = getView().findViewById(R.id.recycle_diet);
        recyclerDiet.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerInt = getView().findViewById(R.id.recycle_intolerances);
        recyclerInt.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCuisine = getView().findViewById(R.id.recycle_cuisine);
        recyclerCuisine.setLayoutManager(new LinearLayoutManager(getContext()));

        ListAdapterRadio mealAdapter = new ListAdapterRadio(mealArray, R.layout.radiobutton, getContext());
        ListAdapterRadio dietAdapter = new ListAdapterRadio(dietArray, R.layout.radiobutton, getContext());
        ListAdapterCheck cuisineAdapter = new ListAdapterCheck(cuisineArray, R.layout.checkbox, getContext());
        ListAdapterCheck intAdapter = new ListAdapterCheck(intolerancesArray, R.layout.checkbox, getContext());

        recyclerCuisine.setAdapter(cuisineAdapter);
        recyclerInt.setAdapter(intAdapter);
        recyclerDiet.setAdapter(dietAdapter);
        recyclerMeal.setAdapter(mealAdapter);

        mealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                dietBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                intBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        dietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dietBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                mealBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                intBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        intolerancesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                mealBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                dietBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        cuisineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                mealBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                dietBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                intBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        query.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(query);
            }
        });
        include.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(include);
            }
        });
        exclude.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(exclude);
            }
        });
        maxFat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(maxFat);
            }
        });
        maxCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(maxCalories);
            }
        });

        include.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
                    exclude.setFocusable(true);
                }
                return false;
            }
        });

        exclude.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
                    maxCalories.requestFocus();
                }
                return false;
            }
        });

        maxCalories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
                    maxFat.requestFocus();
                }
                return false;
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String querySearch = query.getText().toString();
                    String includeIngredients = include.getText().toString();
                    String excludeIngredients = exclude.getText().toString();
                    int maxfat = Integer.parseInt(maxFat.getText().toString());
                    int maxcalories = Integer.parseInt(maxCalories.getText().toString());
                    if(query.getText().toString().matches("")){
                        Toast.makeText(context, "Search query is compulsory", Toast.LENGTH_SHORT).show();
                        query.setFocusable(true);
                    } else {

                        cuisine = cuisineAdapter.getCheckedValue();
                        diet = dietAdapter.getCheckedValue();
                        meal = mealAdapter.getCheckedValue();
                        intolerances = intAdapter.getCheckedValue();

                        searchFood(querySearch, cuisine, diet, meal, intolerances, includeIngredients, excludeIngredients, maxfat, maxcalories);
                    }

                } catch (Exception e) {
                    Log.i("TAG", "onClickBtnFind: EXCEPTION : " + e.getMessage());
                }
            }
        });
    }

    private void searchFood (String searchQuery, String cuisine, String diet, String meal,
                             String intolerances, String includeIng, String excludeIng, int maxF, int maxC) {

        textViewResult = getView().findViewById(R.id.text_view_result);

        Log.d("TAG", "searchFood: " + cuisine + " " + diet + " " + meal + " " + intolerances + " " + searchQuery);

        recycleFood = (RecyclerView) getView().findViewById(R.id.recycle_food);
        recycleFood.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiComplexSearchInterface apiComplexSearchInterface = retrofit.create(ApiComplexSearchInterface.class);

        try {
            Call<ComplexSearch> call = apiComplexSearchInterface.getSearchResult(apiKey, searchQuery, diet,
                    intolerances, includeIng, excludeIng, true, meal, true, maxF, 30, cuisine, maxC);

            call.enqueue(new Callback<ComplexSearch>() {
                @Override
                public void onResponse(Call<ComplexSearch> call, Response<ComplexSearch> response) {
                    if(!response.isSuccessful()){
                        textViewResult.setText("Response code: " + response.code() + call.request().toString());
                        return;
                    }
                    ComplexSearch complexSearch = response.body();
                    Log.i("TAG", "onResponse: url" + response.toString());

                    List<Result> result = complexSearch.getResults();

                    recycleFood.setAdapter(new ComplexAdapter(result, R.layout.food_adapter, getContext()));

                    foodBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }

                @Override
                public void onFailure(Call<ComplexSearch> call, Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("TAG", "onClick: EXCEPTION  : " + e.getMessage());
        }
    }

    private void hideSoftKeyboard(View v) {
        Log.d("TAG", "hideSoftKeyboard: yes");
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}