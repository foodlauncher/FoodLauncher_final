package com.launcher.foodlauncher.ui.find_food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.launcher.foodlauncher.R;

import org.w3c.dom.Text;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private List<Recipe> recipes;
    private Context context;
    private int rowLayout;
    GradientDrawable shape = new GradientDrawable();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textOne;
        TextView textTwo;
        TextView textThree;
        TextView textFour;
        TextView textFive;
        RelativeLayout relativeLayout;
        ViewGroup viewGroup;
        public ViewHolder(View v) {
            super(v);
            textOne = (TextView) v.findViewById(R.id.textView1);
            textTwo = (TextView) v.findViewById(R.id.textView2);
            textThree = (TextView) v.findViewById(R.id.textView3);
            textFour = (TextView) v.findViewById(R.id.textView4);
            textFive = (TextView) v.findViewById(R.id.textView5);
            relativeLayout = v.findViewById(R.id.food_details);
        }
    }

    public FoodAdapter(List<Recipe> recipes, int rowLayout, Context context) {
        this.recipes = recipes;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        if(recipes.size() > 0) {
            StringBuilder diets = new StringBuilder();
            StringBuilder cuisines = new StringBuilder();
            if (recipes.get(position).getDiets().size() > 0) {
                for (int i = 0; i < recipes.get(position).getDiets().size(); i++) {
                    diets.append(recipes.get(position).getDiets().get(i)).append(",");
                }
                diets.deleteCharAt(diets.lastIndexOf(","));
                holder.textTwo.setText("Diets: " + diets.toString());
            } else
                holder.textTwo.setVisibility(View.GONE);
            if (recipes.get(position).getCuisines().size() > 0) {
                for (int i = 0; i < recipes.get(position).getCuisines().size(); i++) {
                    cuisines.append(recipes.get(position).getCuisines().get(i)).append(", ");
                }
                cuisines.deleteCharAt(cuisines.lastIndexOf(","));
                holder.textThree.setText("Cuisines: " + cuisines.toString());
            } else
                holder.textThree.setVisibility(View.GONE);

            holder.textOne.setText(recipes.get(position).getTitle());
            holder.textFive.setText(recipes.get(position).getCreditsText());
            holder.textFour.setText("Ready in " + recipes.get(position).getReadyInMinutes() + " minutes");

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.full_recipe, holder.viewGroup, false);

                    LinearLayout stepsLayout = dialogView.findViewById(R.id.steps);
                    TextView title = dialogView.findViewById(R.id.food_title);
                    TextView credits = dialogView.findViewById(R.id.food_credits);
                    TextView servings = dialogView.findViewById(R.id.food_servings);
                    TextView ready = dialogView.findViewById(R.id.food_ready_in);
                    TextView calories = dialogView.findViewById(R.id.food_calories);
                    TextView fat = dialogView.findViewById(R.id.food_fat);
                    TextView ingredients = dialogView.findViewById(R.id.food_ingredients);

                    title.setText(recipes.get(position).getTitle());
                    credits.setText("Credits: " + recipes.get(position).getCreditsText());
                    servings.setText("Servings: " + recipes.get(position).getServings());
                    ready.setText("Ready in " + recipes.get(position).getReadyInMinutes() + " minutes");
                    calories.setVisibility(View.GONE);
                    fat.setVisibility(View.GONE);

                    StringBuilder ing = new StringBuilder();
                    for (int i = 0; i < recipes.get(position).getAnalyzedInstructions().get(0).getSteps().size(); i++) {
                        for (int j = 0; j < recipes.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getIngredients().size(); j++) {
                            ing.append(recipes.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getIngredients().get(j).getName()).append(", ");
                        }
                    }
                    ing.deleteCharAt(ing.lastIndexOf(","));
                    ingredients.setText("Ingredients: " + ing.toString());

                    int totalSteps = recipes.get(position).getAnalyzedInstructions().get(0).getSteps().size();

                    for (int i = 0; i < totalSteps; i++) {
                        TextView t = new TextView(context);
                        t.setText("Step " + recipes.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getNumber()
                                + ": " + recipes.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getStep());
                        t.setPadding(10, 5, 10, 10);
                        t.setTextSize(15);
                        stepsLayout.addView(t);
                    }
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}