package com.planeat.front_end.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;

import java.util.List;

public class MealRecyclerViewAdapter extends RecyclerView.Adapter {

    Context context;

    public MealRecyclerViewAdapter(Context context) {
        this.context = context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView mealName;
        TextView mealPrepTime;
        TextView mealPerson;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            mealName = (TextView) itemView.findViewById(R.id.MealNameTextView);
            mealPrepTime = (TextView) itemView.findViewById(R.id.MealPrepTimeTextView);
            mealPerson = (TextView) itemView.findViewById(R.id.MealPersonTextView);



        }
    }
}
