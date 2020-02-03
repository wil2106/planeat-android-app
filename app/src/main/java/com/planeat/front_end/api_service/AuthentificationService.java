package com.planeat.front_end.api_service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthentificationService {
    private Context activityContext;

    public AuthentificationService(Context activityContext) {
        this.activityContext = activityContext;
    }
    public void register(){

    }

    public void login(String p_login, String p_password){

        final String login = p_login;
        final String password = p_password;

        String url = activityContext.getString(R.string.server_url)+"/test";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObject = null;
                        try {
                            responseObject = new JSONObject(response);
                            Toast.makeText(activityContext,responseObject.toString(), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*
                        Intent detailIntent = new Intent(activityContext, MainActivity.class);
                        activityContext.startActivity(detailIntent);
                         */
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                        builder.setMessage("Invalid credentials").setTitle("Alert");
                        AlertDialog dialog = builder.create();
                        dialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);
                return params;
            }
        };
        NetworkSingleton.getInstance(activityContext).addToRequestQueue(postRequest);
    }
}
