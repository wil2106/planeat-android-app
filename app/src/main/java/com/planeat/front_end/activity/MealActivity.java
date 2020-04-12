package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.MealIngredientRecyclerViewAdapter;
import com.planeat.front_end.utils.MealRecipeRecyclerViewAdapter;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MealActivity extends AppCompatActivity {

    String token;
    String recipeId;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        recipeId = getIntent().getStringExtra("recipeId");
        ImageView returnBtn = (ImageView) findViewById(R.id.buttonMealReturn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(MealActivity.this, MainActivity.class);
                startActivity(returnIntent);
            }
        });

        String url = getString(R.string.server_url) + "/recipes/" + recipeId + "/details";

        final TextView mealNameTV = (TextView) findViewById(R.id.mealNameTextView);
        final TextView mealPersonTV = (TextView) findViewById(R.id.mealPersonTextView);
        final TextView mealPrepTimeTV = (TextView) findViewById(R.id.mealPrepTimeTextView);
        final TextView mealDescriptionTV = (TextView) findViewById(R.id.mealDescriptionTextView);

        final RecyclerView ingredientRV = (RecyclerView) findViewById(R.id.ingredientList);
        final RecyclerView recipeRV = (RecyclerView) findViewById(R.id.recipeList);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager ingredientLinearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager recipeLinearLayoutManager = new LinearLayoutManager(this);
        ingredientRV.setLayoutManager(ingredientLinearLayoutManager);
        recipeRV.setLayoutManager(recipeLinearLayoutManager);

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);
        JsonArrayRequest testRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject resp = response.getJSONObject(0);
                    mealNameTV.setText(resp.getString("recipe_name"));
                    mealPersonTV.setText(resp.getString("recipe_nb_servings"));
                    mealPrepTimeTV.setText(resp.getString("recipe_prep_time"));
                    mealDescriptionTV.setText(resp.getString("recipe_description"));

                    MealIngredientRecyclerViewAdapter mealIngredientAdapter = new MealIngredientRecyclerViewAdapter(context, resp.getJSONArray("ingredients"));
                    MealRecipeRecyclerViewAdapter mealRecipeAdapter = new MealRecipeRecyclerViewAdapter(context, resp.getJSONArray("steps"));

                    ingredientRV.setAdapter(mealIngredientAdapter); // set the Adapter to RecyclerView
                    recipeRV.setAdapter(mealRecipeAdapter); // set the Adapter to RecyclerView

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mealNameTV.setText("Something went wrong");
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
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(testRequest);
    }
}
