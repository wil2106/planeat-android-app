package com.planeat.front_end.mainactivity_fragments.groceries;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.activity.ShoppingListActivity;
import com.planeat.front_end.activity.ShoppingListCreationActivity;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class GroceriesFragment extends Fragment {

    private GroceriesViewModel groceriesViewModel;
    private String token;
    private String userId;
    private Activity activity;
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
        groceriesViewModel = ViewModelProviders.of(this).get(GroceriesViewModel.class);
        root = inflater.inflate(R.layout.fragment_groceries, container, false);

        ImageView button = (ImageView) root.findViewById(R.id.imageViewCreateShoppingList);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shoppingListIntent = new Intent(getContext(), ShoppingListCreationActivity.class);
                getContext().startActivity(shoppingListIntent);
            }
        });

        getUserIdThenFireShoppingListsRequest();

        return root;
    }


    public void getShoppingLists(){
        if(userId!=null){
            String url = "https://planeat-nodejs-backend.appspot.com/shoppingLists?user_id="+userId;

            /* Get access token from shared preferences */
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
            token = sharedPreferences.getString("access_token", null);

            final List<JSONObject> shoppingLists = new ArrayList();

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
                            shoppingLists.add(response.getJSONObject(i));
                            i++;
                        }

                        //Collections.shuffle(recipeList);

                        //List<JSONObject> subRecipeList = shoppingLists.subList(0,10);

                        //  call the constructor of MealsAdapter to send the reference and data to Adapter
                        GroceriesAdapter groceriesAdapter = new GroceriesAdapter(getContext(), shoppingLists);
                        recyclerView.setAdapter(groceriesAdapter); // set the Adapter to RecyclerView

                        /* Hide bar */
                        final ProgressBar progressBar = activity.findViewById(R.id.progressBar);
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

            NetworkSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(recipeRequest);
        }else{
            new AlertDialog.Builder(activity).setTitle("Alert").setMessage("Something went wrong, user id null")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    public void getUserIdThenFireShoppingListsRequest(){
        String url = getString(R.string.server_url) + "/users/profile";

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        JsonArrayRequest userIdRequest = new JsonArrayRequest(Request.Method.GET, url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userId = response.getJSONObject(0).getString("user_id");
                    getShoppingLists();
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