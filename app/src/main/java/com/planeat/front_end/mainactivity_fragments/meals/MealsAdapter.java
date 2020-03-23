package com.planeat.front_end.mainactivity_fragments.meals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;

import java.util.ArrayList;
import java.util.List;

class MealsAdapter extends RecyclerView.Adapter {

    List<String> recipes;
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
        String entryInfo = recipes.get(position);
        myViewHolder.name.setText(entryInfo);
    }
    @Override
    public int getItemCount() {;
        return recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.recipe_name);
        }
    }
}
