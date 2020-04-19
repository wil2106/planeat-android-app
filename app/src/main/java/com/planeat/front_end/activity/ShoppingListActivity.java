package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.NetworkSingleton;
import com.planeat.front_end.utils.ShoppingListAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {
    private String shoppingListId;
    private String token;
    private double totalPriceValue;
    private ShoppingListActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.planeat_green)));

        shoppingListId = getIntent().getStringExtra("shoppingListId");
        activity = this;
        getItems();
    }

    public void getItems(){
        String url = "https://planeat-nodejs-backend.appspot.com/shoppingLists/"+shoppingListId+"/details";

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        final List<JSONObject> items = new ArrayList();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        final TextView shoppingListDates = (TextView) findViewById(R.id.ShoppingListDatesTextView);
        final TextView totalPrice = (TextView) findViewById(R.id.totalPriceTextView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        JsonArrayRequest shoppingListItemsRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    setTitle(response.getJSONObject(0).getString("shoppinglist_name"));

                    shoppingListDates.setText(response.getJSONObject(0).getString("shoppinglist_start_date")+" - "+response.getJSONObject(0).getString("shoppinglist_end_date"));


                    JSONArray articlesJsonArray = response.getJSONObject(0).getJSONArray("articles");
                    ArrayList<JSONObject> articles = new ArrayList<JSONObject>();
                    if (articlesJsonArray != null) {
                        for (int i=0;i<articlesJsonArray.length();i++){
                            articles.add(articlesJsonArray.getJSONObject(i));
                        }
                    }

                    //  call the constructor of MealsAdapter to send the reference and data to Adapter
                    ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(getApplicationContext(), articles,activity);
                    recyclerView.setAdapter(shoppingListAdapter); // set the Adapter to RecyclerView
                    recyclerView.getViewTreeObserver()
                            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    totalPrice.setText("Estimated total price: "+totalPriceValue+" €");
                                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });

                    /* Hide bar */
                    final ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("meh.", "il y a une erreur");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(shoppingListItemsRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /* *****  Go to previous view in the stack if back button is clicked  ***** */
                super.onBackPressed();
                return true;
        }
        return false;
    }

    public void setTotalPrice(double totalPrice){
        Log.i("#############", "price: "+totalPrice);
        this.totalPriceValue = totalPrice;
    }
}
