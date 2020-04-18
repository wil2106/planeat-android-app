package com.planeat.front_end.mainactivity_fragments.meals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.activity.LoginActivity;
import com.planeat.front_end.activity.MainActivity;
import com.planeat.front_end.activity.MealActivity;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class MealsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private MealsViewModel mealsViewModel;
    private Activity activity;
    private String token;
    private String userId;
    private View root;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity =(Activity) context; // here, we get the instance of the current activity to use its functions
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        mealsViewModel = ViewModelProviders.of(this).get(MealsViewModel.class);
        root = inflater.inflate(R.layout.fragment_meals, container, false);
        setHasOptionsMenu(true);



        getUserIdThenFireMealsRequest();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_meal_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_meal_bar);
        SearchView mealsSV = (SearchView) menuItem.getActionView();
        mealsSV.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String mealSearchInput = newText.toLowerCase();

        String url = getString(R.string.server_url) + "/recipes/search?keyword=" + mealSearchInput;

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        final List<JSONObject> recipeListSearch = new ArrayList();

        final RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.list);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        JsonArrayRequest recipeRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                    int i =0;
                    while(response.length() > i) {
                        recipeListSearch.add(response.getJSONObject(i));
                        i++;
                    }

                    //  call the constructor of MealsAdapter to send the reference and data to Adapter
                    MealsAdapter mealsAdapter = new MealsAdapter(getContext(), recipeListSearch);
                    recyclerView.setAdapter(mealsAdapter); // set the Adapter to RecyclerView

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

        NetworkSingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(recipeRequest);


        return false;
    }

    public void getMeals(){
        if(userId!=null){

            String url = "https://europe-west1-planeat-nodejs-backend.cloudfunctions.net/planeatMLFunction?id="+userId;

            /* Get access token from shared preferences */
            //SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
            //token = sharedPreferences.getString("access_token", null);


            final List<JSONObject> recipeList = new ArrayList();

            final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
            // set a LinearLayoutManager with default vertical orientation
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            JsonArrayRequest recipeRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        int i =0;
                        while(response.length() > i) {
                            recipeList.add(response.getJSONObject(i));
                            i++;
                        }

                        //Collections.shuffle(recipeList);

                        List<JSONObject> subRecipeList = recipeList.subList(0,10);

                        //  call the constructor of MealsAdapter to send the reference and data to Adapter
                        MealsAdapter mealsAdapter = new MealsAdapter(getContext(), subRecipeList);
                        recyclerView.setAdapter(mealsAdapter); // set the Adapter to RecyclerView

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
                    //params.put("Content-Type", "application/json; charset=UTF-8");
                    //params.put("Authorization", "Bearer " + token);
                    return params;
                }
            };

            NetworkSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(recipeRequest);
        }else{
            new AlertDialog.Builder(activity).setTitle("Alert").setMessage("Something went wrong, user id null")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    public void getUserIdThenFireMealsRequest(){
        String url = getString(R.string.server_url) + "/users/profile";

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        JsonArrayRequest userIdRequest = new JsonArrayRequest(Request.Method.GET, url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userId = response.getJSONObject(0).getString("user_id");
                    getMeals();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(),"Error"+error.getMessage(),Toast.LENGTH_SHORT).show();
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


        NetworkSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(userIdRequest);
    }
}