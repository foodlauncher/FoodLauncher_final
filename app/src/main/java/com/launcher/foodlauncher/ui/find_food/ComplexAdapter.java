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

public class ComplexAdapter extends RecyclerView.Adapter<ComplexAdapter.ViewHolder>{

    private List<Result> result;
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

    public ComplexAdapter(List<Result> result, int rowLayout, Context context) {
        this.result = result;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public ComplexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ComplexAdapter.ViewHolder holder, int position) {
        StringBuilder diets = new StringBuilder();
        StringBuilder cuisines = new StringBuilder();
        if(result.get(position).getDiets().size() > 0) {
            for(int i=0; i<result.get(position).getDiets().size(); i++) {
                diets.append(result.get(position).getDiets().get(i)).append(",");
            } diets.deleteCharAt(diets.lastIndexOf(","));
            holder.textTwo.setText("Diets: " + diets.toString());
        } else
            holder.textTwo.setVisibility(View.GONE);
        if(result.get(position).getCuisines().size() > 0) {
            for(int i=0; i<result.get(position).getCuisines().size(); i++) {
                cuisines.append(result.get(position).getCuisines().get(i)).append(", ");
            } cuisines.deleteCharAt(cuisines.lastIndexOf(","));
            holder.textThree.setText("Cuisines: " + cuisines.toString());
        } else
            holder.textThree.setVisibility(View.GONE);

        holder.textOne.setText(result.get(position).getTitle());
        holder.textFive.setText(result.get(position).getCreditsText());
        holder.textFour.setText("Ready in " + result.get(position).getReadyInMinutes() + " minutes");

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

                title.setText(result.get(position).getTitle());
                credits.setText("Credits: " + result.get(position).getCreditsText());
                servings.setText("Servings: " + result.get(position).getServings());
                ready.setText("Ready in " + result.get(position).getReadyInMinutes() + " minutes");
                calories.setText("Total calories: " + result.get(position).getNutrition().get(0).getAmount() + " cal");
                fat.setText("Total Fat: " + result.get(position).getNutrition().get(1).getAmount() + " g");

                StringBuilder ing = new StringBuilder();
                for(int i=0; i<result.get(position).getAnalyzedInstructions().get(0).getSteps().size(); i++) {
                    for (int j = 0; j < result.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getIngredients().size(); j++){
                        ing.append(result.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getIngredients().get(j).getName()).append(", ");
                    }
                } ing.deleteCharAt(ing.lastIndexOf(","));
                ingredients.setText("Ingredients: " + ing.toString());

                int totalSteps = result.get(position).getAnalyzedInstructions().get(0).getSteps().size();

                for(int i=0; i<totalSteps; i++) {
                    TextView t = new TextView(context);
                    t.setText("Step " + result.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getNumber()
                            + ": " + result.get(position).getAnalyzedInstructions().get(0).getSteps().get(i).getStep());
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

    @Override
    public int getItemCount() {
        return result.size();
    }
}