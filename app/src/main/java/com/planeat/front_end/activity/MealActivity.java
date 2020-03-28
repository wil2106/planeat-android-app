package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.mainactivity_fragments.meals.MealsFragment;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MealActivity extends AppCompatActivity {

    String token;
    String recipeId;

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

        String url = getString(R.string.server_url) + "/recipes/" + recipeId;
        final TextView mealNameTV = (TextView) findViewById(R.id.mealNameTextView);

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);
        JsonArrayRequest testRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    mealNameTV.setText(response.getJSONObject(0).getString("recipe_name"));
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
