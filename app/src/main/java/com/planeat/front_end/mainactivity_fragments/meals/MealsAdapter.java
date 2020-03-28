package com.planeat.front_end.mainactivity_fragments.meals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;
import com.planeat.front_end.activity.MealActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

class MealsAdapter extends RecyclerView.Adapter {

    List<JSONObject> recipes;
    Context context;

    public MealsAdapter(Context context, List recipes) {
        this.context = context;
        this.recipes = recipes;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String entryName = null;
        String entryTime = null;
        String entryPerson = null;
        try {
            entryName = recipes.get(position).getString("recipe_name");
            entryTime = recipes.get(position).getString("recipe_prep_time");
            entryPerson = recipes.get(position).getString("recipe_nb_servings");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        myViewHolder.mealName.setText(entryName);
        myViewHolder.mealPrepTime.setText(entryTime + "' - ");
        // In case the meal is for one person
        if(!(entryPerson == "1"))
            myViewHolder.mealPerson.setText(entryPerson + " personne");
        myViewHolder.mealPerson.setText(entryPerson + " personnes");

        final RelativeLayout meal = (RelativeLayout) holder.itemView.findViewById(R.id.buttonMeal);
        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mealIntent = new Intent(context, MealActivity.class);
                try {
                    mealIntent.putExtra("recipeId", recipes.get(position).getString("recipe_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(mealIntent);
            }
        });
    }
    @Override
    public int getItemCount() {;
        return recipes.size();
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
