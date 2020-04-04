package com.planeat.front_end.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
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

public class AgendaRecyclerViewAdapter extends RecyclerView.Adapter<AgendaRecyclerViewAdapter.ViewHolder> {

    private List<JSONObject> mData;
    private LayoutInflater mInflater;
    Context context;

    // data is passed into the constructor
    public AgendaRecyclerViewAdapter(Context context, List<JSONObject> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.agenda_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JSONObject agendaInfo = mData.get(position);
        try {
            holder.nameTV.setText(agendaInfo.getString("recipe_name"));
            holder.numberTV.setText(agendaInfo.getString("recipe_nb_servings"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RelativeLayout meal = (RelativeLayout) holder.itemView.findViewById(R.id.agendaMeal);
        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mealIntent = new Intent(context, MealActivity.class);
                try {
                    mealIntent.putExtra("recipeId", agendaInfo.getString("recipe_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(mealIntent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView numberTV;

        ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.agendaRecipeNameTextView);
            numberTV = itemView.findViewById(R.id.agendaRecipeNumberTextView);
        }
    }

}
