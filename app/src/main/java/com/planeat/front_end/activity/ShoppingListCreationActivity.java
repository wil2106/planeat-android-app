package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListCreationActivity extends AppCompatActivity {
    private String token;
    private String userId;
    private ShoppingListCreationActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_creation);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.planeat_green)));

        setTitle("Shopping list creation");

        activity = this;

        final RadioButton oneMarketRadioButton = (RadioButton) findViewById(R.id.oneMarketRadio);
        final RadioButton lowestPricesRadio = (RadioButton) findViewById(R.id.lowestPricesRadio);

        final TextView marketTextView = (TextView) findViewById(R.id.marketTextView);
        final EditText marketEditText = (EditText) findViewById(R.id.marketEditText);

        lowestPricesRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                marketTextView.setVisibility(View.INVISIBLE);
                marketEditText.setVisibility(View.INVISIBLE);
            }
        });

        oneMarketRadioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                marketTextView.setVisibility(View.VISIBLE);
                marketEditText.setVisibility(View.VISIBLE);
            }
        });

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


    public void generateshoppingList(){
        final RadioButton oneMarketRadioButton = (RadioButton) findViewById(R.id.oneMarketRadio);
        final RadioButton lowestPricesRadio = (RadioButton) findViewById(R.id.lowestPricesRadio);

        final TextView marketTextView = (TextView) findViewById(R.id.marketTextView);
        final EditText marketEditText = (EditText) findViewById(R.id.marketEditText);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        final DatePicker startDate = (DatePicker) findViewById(R.id.startDateDatePicker);
        final DatePicker endDate = (DatePicker) findViewById(R.id.endDateDatePicker);

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        String url = getString(R.string.server_url)+"/shoppingLists/generate?";
        url+="user_id="+userId;
        DecimalFormat df = new DecimalFormat("00");
        url+="&shoppinglist_start_date="+df.format(startDate.getYear())+"-"+df.format(startDate.getMonth())+"-"+df.format(startDate.getDayOfMonth());
        url+="&shoppinglist_end_date="+df.format(endDate.getYear())+"-"+df.format(endDate.getMonth())+"-"+df.format(endDate.getDayOfMonth());
        url+="&lowest_price="+lowestPricesRadio.isChecked();
        url+="&shop="+marketEditText.getText().toString().toUpperCase();

        //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();

        StringRequest generateShoppingListRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Shopping List created !",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Shopping List creation failed ! error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
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
        };
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(generateShoppingListRequest);
    }

    public void cancelCreation(View view) {
        activity.finish();
    }

    public void createShoppingList(View view) {
        getUserIdThenFireShoppingListCreation();
    }

    public void getUserIdThenFireShoppingListCreation(){
        String url = getString(R.string.server_url) + "/users/profile";

        /* Get access token from shared preferences */
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);

        JsonArrayRequest userIdRequest = new JsonArrayRequest(Request.Method.GET, url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    userId = response.getJSONObject(0).getString("user_id");
                    generateshoppingList();
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
