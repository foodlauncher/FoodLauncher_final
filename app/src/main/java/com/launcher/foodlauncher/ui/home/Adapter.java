package com.launcher.foodlauncher.ui.home;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.launcher.foodlauncher.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<Restaurant> restaurants;
    private Context context;
    private int rowLayout;
    GradientDrawable shape = new GradientDrawable();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textOne;
        TextView textTwo;
        TextView textThree;
        ImageView resImage;
        ViewGroup viewGroup;
        RelativeLayout relativeLayout;
        public ViewHolder(View v) {
            super(v);
            textOne = (TextView) v.findViewById(R.id.textView1);
            textTwo = (TextView) v.findViewById(R.id.textView2);
            textThree = (TextView) v.findViewById(R.id.textView3);
            resImage = v.findViewById(R.id.res_image);
            relativeLayout = v.findViewById(R.id.rest_details);
            viewGroup = v.findViewById(android.R.id.content);
        }
    }

    public Adapter(List<Restaurant> restaurants, int rowLayout, Context context) {
        this.restaurants = restaurants;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        holder.textOne.setText(restaurants.get(position).getRestaurant().getName());
        holder.textTwo.setText(restaurants.get(position).getRestaurant().getLocation().getAddress());
        holder.textThree.setText("Rating: " + restaurants.get(position).getRestaurant().getUserRating().getAggregateRating());
//        if(restaurants.get(position).getRestaurant().getHasTableBooking() == 0) {
//            holder.textFive.setText("Table booking not available");
//        } else {
//            holder.textFive.setText("Table booking available");
//        }

        if(restaurants.get(position).getRestaurant().getThumb() != "") {
            Glide.with(context)
                    .load(restaurants.get(position).getRestaurant().getThumb())
                    .into(holder.resImage);
        }

//        double lat = Double.parseDouble(restaurants.get(position).getRestaurant().getLocation().getLatitude());
//        double lon = Double.parseDouble(restaurants.get(position).getRestaurant().getLocation().getLongitude());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.restaurant_details, holder.viewGroup, false);

                TextView resName = dialogView.findViewById(R.id.res_name);
                TextView resCuisines = dialogView.findViewById(R.id.res_cuisines);
                TextView resTimings = dialogView.findViewById(R.id.res_timings);
                TextView resRating = dialogView.findViewById(R.id.res_rating);
                TextView resAddress = dialogView.findViewById(R.id.res_address);
                TextView resMenu = dialogView.findViewById(R.id.res_menu);
                TextView resPhone = dialogView.findViewById(R.id.res_phone);
                Button callBtn = dialogView.findViewById(R.id.call_now);
                Button inviteBtn = dialogView.findViewById(R.id.invite);
                TextView costForTwo = dialogView.findViewById(R.id.res_cost_for_two);
                TextView tableReservation = dialogView.findViewById(R.id.res_table);
                RelativeLayout rate = dialogView.findViewById(R.id.rel_rating);

                resName.setText(restaurants.get(position).getRestaurant().getName());
                resAddress.setText(restaurants.get(position).getRestaurant().getLocation().getAddress());
                resCuisines.setText(restaurants.get(position).getRestaurant().getCuisines());
                resTimings.setText("Open Hours: " + restaurants.get(position).getRestaurant().getTimings());
                resMenu.setText("Open menu");
                resRating.setText(restaurants.get(position).getRestaurant().getUserRating().getAggregateRating());
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadii(new float[] { 16, 16, 16, 16, 16, 16, 16, 16 });
                shape.setColor(Color.parseColor("#" + restaurants.get(position).getRestaurant().getUserRating().getRatingColor()));
                rate.setBackground(shape);
                costForTwo.setText("Average cost for two: Rs. " + restaurants.get(position).getRestaurant().getAverageCostForTwo());
                if(restaurants.get(position).getRestaurant().getIsTableReservationSupported() == 0) {
                    tableReservation.setText("Table reservation is not supported.");
                } else {
                    tableReservation.setText("Table reservation is not supported.");
                }

                inviteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWhatsApp(v);
                    }
                });

                resMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uriUrl = Uri.parse(restaurants.get(position).getRestaurant().getMenuUrl());
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        launchBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                        context.startActivity(launchBrowser);
                    }
                });

                if(!restaurants.get(position).getRestaurant().getPhoneNumbers().equals("Not available for this place")) {
                    resPhone.setText("Phone number: " + restaurants.get(position).getRestaurant().getPhoneNumbers());
                    Uri u = Uri.parse("tel:" + restaurants.get(position).getRestaurant().getPhoneNumbers());
                    callBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_DIAL, u);
                            try
                            {
                                // Launch the Phone app's dialer with a phone
                                // number to dial a call.
                                context.startActivity(i);
                            }
                            catch (SecurityException s)
                            {
                                // show() method display the toast with
                                // exception message.
                                Toast.makeText(context, "Error: " + s.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    resPhone.setText("Phone number not available for this place.");
                    callBtn.setVisibility(View.INVISIBLE);
                }

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    void openWhatsApp(View view){
        PackageManager pm = context.getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "I'm inviting you to my party."; // Replace with your own message.

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }catch(Exception e){
            e.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}