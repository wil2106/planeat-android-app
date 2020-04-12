package com.planeat.front_end.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MealRecipeRecyclerViewAdapter extends RecyclerView.Adapter {

    Context context;
    JSONArray steps;


    public MealRecipeRecyclerViewAdapter(Context context, JSONArray steps) {
        this.context = context;
        this.steps = steps;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_recipe_recyclerview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String entryRecipeStepNumber = null;
        String entryRecipeStepDescription = null;

        try {
            entryRecipeStepNumber = steps.getJSONObject(position).getString("step_order_number");
            entryRecipeStepDescription = steps.getJSONObject(position).getString("step_description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myViewHolder.recipeStepNumberTV.setText("Etape " + entryRecipeStepNumber + " :");
        myViewHolder.recipeStepDescriptionTV.setText(entryRecipeStepDescription);
    }

    @Override
    public int getItemCount() {
        return steps.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView recipeStepNumberTV;
        TextView recipeStepDescriptionTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            recipeStepNumberTV = (TextView) itemView.findViewById(R.id.RecipeStepNumberTextView);
            recipeStepDescriptionTV = (TextView) itemView.findViewById(R.id.RecipeStepDescriptionTextView);



        }
    }
}
