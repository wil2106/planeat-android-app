package com.planeat.front_end.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealIngredientRecyclerViewAdapter extends RecyclerView.Adapter {

    Context context;
    JSONArray ingredients;

    public MealIngredientRecyclerViewAdapter(Context context, JSONArray ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_ingredient_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String entryIngredientName = null;
        String entryIngredientQuantity = null;
        String entryIngredientQuantityType = null;

        try {
            entryIngredientName = ingredients.getJSONObject(position).getJSONObject("product").getString("name");
            entryIngredientQuantity = ingredients.getJSONObject(position).getString("quantity");
            entryIngredientQuantityType = ingredients.getJSONObject(0).getJSONObject("quantity_type").getString("name");

            Log.i("Name", ingredients.getJSONObject(position).getJSONObject("product").getString("name"));
            Log.i("quantity", ingredients.getJSONObject(position).getString("quantity"));
            Log.i("quantity type", ingredients.getJSONObject(0).getJSONObject("quantity_type").getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        myViewHolder.ingredientName.setText(entryIngredientName.substring(0, 1).toUpperCase() + entryIngredientName.substring(1) + " - ");
        myViewHolder.ingredientQuantity.setText(entryIngredientQuantity + " ");
        myViewHolder.ingredientQuantityType.setText(entryIngredientQuantityType);
    }

    @Override
    public int getItemCount() {
        return ingredients.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientQuantityType;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ingredientName = (TextView) itemView.findViewById(R.id.IngredientNameTextView);
            ingredientQuantity = (TextView) itemView.findViewById(R.id.IngredientQuantityTextView);
            ingredientQuantityType = (TextView) itemView.findViewById(R.id.IngredientQuantityTypeTextView);
        }
    }
}
