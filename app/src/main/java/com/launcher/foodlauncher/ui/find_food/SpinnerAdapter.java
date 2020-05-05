package com.launcher.foodlauncher.ui.find_food;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class SpinnerAdapter extends ArrayAdapter {

    private SparseBooleanArray checked = new SparseBooleanArray();
    private SparseArray<String> array;
    private TextView titleView;

    SpinnerAdapter(@NonNull Context context, SparseArray<String> array, String title) {
        super(context, 0);
        this.array = array;
        titleView = new TextView(context);
        titleView.setText(title);
        titleView.setPadding(10, 3, 10, 3);
    }

    void setCheckedArray(SparseBooleanArray checked){
        this.checked = checked;
    }

    Integer getCheckedKeys(){
        Integer integer = 0;
        if(checked.size() > 0){
            integer = array.keyAt(checked.keyAt(0));
            for(int index = 1; index < checked.size(); index++){
                integer = integer | array.keyAt(checked.keyAt(index));
            }
        }
        return integer;
    }

    String getCheckedValues(){
        StringBuilder string = new StringBuilder();
        if(checked.size() > 0){
            for(int index = 0; index < checked.size(); index++){
                string.append(array.valueAt(checked.keyAt(index)));
            }
        }
        return string.toString();
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView title;
    }

    private void handleDropDownHolder(final int position, ViewHolder holder){
        holder.title.setText(getItem(position));
        holder.checkBox.setChecked(checked.get(position, false));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View checkBox) {
                setChecked(checkBox, position);
            }
        });
    }

    void setChecked(View view, int position){
        if(checked.get(position, false)){
            ((CheckBox) view).setChecked(false);
            checked.delete(position);
        } else {
            ((CheckBox) view).setChecked(true);
            checked.put(position, true);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return array.valueAt(position);
    }

    class Item extends LinearLayout {
        ViewHolder holder;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 80);
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        Item(Context context) {
            super(context);
            setLayoutParams(params);
            setOrientation(LinearLayout.HORIZONTAL);
            setBackgroundColor(Color.WHITE);
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
            CheckBox checkBox = new CheckBox(context);
            TextView title = new TextView(context);
            title.setLayoutParams(params);
            holder = new ViewHolder();
            holder.title = title;
            holder.checkBox = checkBox;
            addView(checkBox);
            addView(title);
        }

        ViewHolder getHolder(){
            return holder;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public @NonNull View getDropDownView(int position, View view, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        if(view == null){
            view = new Item(getContext());
        }
        handleDropDownHolder(position, ((Item) view).getHolder());
        return view;
    }

    @Override
    public @NonNull View getView(int position, View view, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return titleView;
    }
}