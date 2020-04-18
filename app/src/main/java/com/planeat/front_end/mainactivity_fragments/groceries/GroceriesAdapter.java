package com.planeat.front_end.mainactivity_fragments.groceries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;
import com.planeat.front_end.activity.MealActivity;
import com.planeat.front_end.activity.ShoppingListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GroceriesAdapter extends RecyclerView.Adapter {

    List<JSONObject> shoppingLists;
    Context context;

    public GroceriesAdapter(Context context, List shoppingLists) {
        this.context = context;
        this.shoppingLists = shoppingLists;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shoppinglist_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String name = null;
        String startDate = null;
        String endDate = null;
        try {
            name = shoppingLists.get(position).getString("shoppinglist_name");
            startDate = shoppingLists.get(position).getString("shoppinglist_start_date");
            endDate = shoppingLists.get(position).getString("shoppinglist_end_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        myViewHolder.shoppingListName.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
        myViewHolder.shoppingListDates.setText("from: "+startDate+"  to: "+endDate);

        final RelativeLayout meal = (RelativeLayout) holder.itemView.findViewById(R.id.buttonShopingList);
        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingListIntent = new Intent(context, ShoppingListActivity.class);
                try {
                    shoppingListIntent.putExtra("shoppingListId", shoppingLists.get(position).getString("shoppinglist_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(shoppingListIntent);
            }
        });
    }
    @Override
    public int getItemCount() {;
        return shoppingLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView shoppingListName;
        TextView shoppingListDates;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            shoppingListName = (TextView) itemView.findViewById(R.id.ShoppingListNameTextView);
            shoppingListDates = (TextView) itemView.findViewById(R.id.ShoppingListDatesTextView);
        }
    }

    public void updateListMeal(List<JSONObject> mealsList){
        shoppingLists = new ArrayList<>();
        shoppingLists.addAll(mealsList);
        notifyDataSetChanged();
    }
}
