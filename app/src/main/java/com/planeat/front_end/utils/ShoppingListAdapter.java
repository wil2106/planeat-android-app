package com.planeat.front_end.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;
import com.planeat.front_end.activity.ShoppingListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter {

    private List<JSONObject> items;
    private Context context;
    private Double totalPrice;
    private ShoppingListActivity activity;
    public ShoppingListAdapter(Context context, List items,ShoppingListActivity activity) {
        this.context = context;
        this.items = items;
        this.totalPrice = 0.0;
        this.activity = activity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shoppinglistitem_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        int itemQuantity = 0;
        double articleQuantity = 0.0;
        double articlePrice = 0.0;
        String articleQuantityType = null;
        String productName = null;
        String brandName = null;
        String marketName = null;
        String quantityType = null;

        try {
            if(items.get(position)!=null){
                itemQuantity = items.get(position).getInt("quantity");

                JSONObject articleDetails = items.get(position).getJSONObject("article_details");
                articleQuantity = articleDetails.getDouble("quantity");
                articlePrice = articleDetails.getDouble("price");

                JSONObject product = articleDetails.getJSONObject("product");
                productName = product.getString("name");

                JSONObject brand = articleDetails.getJSONObject("brand");
                brandName = brand.getString("name");

                JSONObject market = articleDetails.getJSONObject("market");
                marketName = market.getString("name");

                JSONObject quantityTypeObject = articleDetails.getJSONObject("quantity_type");
                articleQuantityType = quantityTypeObject.getString("quantitytype_name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myViewHolder.articlePrice.setText(articlePrice+" €");
        myViewHolder.articleQuantity.setText(articleQuantity+" "+articleQuantityType);
        myViewHolder.brandName.setText(brandName);
        myViewHolder.marketName.setText(marketName);
        myViewHolder.productName.setText(productName);
        myViewHolder.itemQuantity.setText("X"+itemQuantity);
        totalPrice+=itemQuantity*articlePrice;
        activity.setTotalPrice(totalPrice);
    }
    @Override
    public int getItemCount() {;
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView shoppingListName;
        TextView shoppingListDates;

        TextView itemQuantity;
        TextView articleQuantity;
        TextView articlePrice;
        TextView articleQuantityType ;
        TextView productName;
        TextView brandName;
        TextView marketName;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantityTextView);
            articleQuantity = (TextView) itemView.findViewById(R.id.articleQuantityTextView);
            articlePrice = (TextView) itemView.findViewById(R.id.articlePriceTextView);
            articleQuantityType = (TextView) itemView.findViewById(R.id.articleQuantityTextView);
            productName = (TextView) itemView.findViewById(R.id.productNameTextView);
            brandName = (TextView) itemView.findViewById(R.id.brandNameTextView);
            marketName = (TextView) itemView.findViewById(R.id.marketNameTextView);
        }
    }

    public void updateListMeal(List<JSONObject> itemsList){
        items = new ArrayList<>();
        items.addAll(itemsList);
        notifyDataSetChanged();
    }
}
