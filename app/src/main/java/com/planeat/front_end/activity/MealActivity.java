package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.MealIngredientRecyclerViewAdapter;
import com.planeat.front_end.utils.MealRecipeRecyclerViewAdapter;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
                onBackPressed();
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
        ImageView addRedirection = (ImageView) findViewById(R.id.addMealToPlanningRedirection);
        addRedirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollView = findViewById(R.id.mealDetailsScrollView);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        Button addToPlanningBtn = (Button) findViewById(R.id.addMealToPlanningButton);
        addToPlanningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker dateP = (DatePicker) findViewById(R.id.mealDetailsDatePicker);
                final Spinner spinner = findViewById(R.id.spinnerMealType);
                String month = (dateP.getMonth() + 1) + "";
                if (dateP.getMonth() + 1 < 10) {
                    month = "0" + (dateP.getMonth() + 1);
                }
                String day = dateP.getDayOfMonth() + "";
                if (dateP.getDayOfMonth() < 10) {
                    day = "0" + dateP.getDayOfMonth();
                }
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("date", dateP.getYear() + "-" + month + "-" + day);
                    jsonObject.put("user_id", NetworkSingleton.getInstance(getApplicationContext()).getUserId());
                    jsonObject.put("recipe_id", recipeId);
                    jsonObject.put("mealtype_id", (spinner.getSelectedItemPosition() + 1));
                } catch (JSONException e) {
                    // handle exception (not supposed to happen)
                }
                String url = getString(R.string.server_url)+"/planning";
                StringRequest postMealRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Do nothing
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("meh", "failed to add meal to planning");
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + token);
                        return params;
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=UTF-8";
                    }
                    @Override
                    public byte[] getBody() {
                        try {
                            return jsonObject.toString().getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            // not supposed to happen
                            return null;
                        }
                    }
                };
                NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postMealRequest);
                Toast.makeText(getApplicationContext(),"Successfully added to planning", Toast.LENGTH_LONG).show();
            }
        });
    }
}
