package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        applicationContext = this.getApplicationContext();
    }

    @Override
    public void onClick(View v) {

        //when user push register button
        if (v.getId() == R.id.btn_register){
            final String login = ((EditText)findViewById(R.id.editText_login)).getText().toString();
            final String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();
            final String email = ((EditText)findViewById(R.id.editText_email)).getText().toString();
            final String firstname = ((EditText)findViewById(R.id.editText_firstName)).getText().toString();
            final String lastname = ((EditText)findViewById(R.id.editText_lastName)).getText().toString();

            if(login.trim().length()>0 && password.trim().length()>0 && email.trim().length()>0 && firstname.trim().length()>0 && lastname.trim().length()>0){
                register(login, password,email,firstname,lastname);
            }else{
                //Error message
                new AlertDialog.Builder(RegisterActivity.this).setTitle("Alert").setMessage("Please fill in all required fields")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }


        if(v.getId() == R.id.textView_login){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//if activity already in the stack, recover it and close activities on top of it
            startActivity(intent);
        }
    }


    public void register(final String login,final String password, final String email,final String firstName,final String lastName){


        String url = getString(R.string.server_url)+"/register";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                //if response status = 200
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObject = null;
                        try {
                            responseObject = new JSONObject(response);
                            new AlertDialog.Builder(RegisterActivity.this).setTitle("Info").setMessage(response)
                                    .setPositiveButton("OK", null)
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//if activity already in the stack, recover it and close activities on top of it
                        startActivity(intent);

                    }
                },
                //if response status != 200
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*
                        JSONObject responseObject = null;
                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                            responseObject = new JSONObject(responseBody);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                        */
                        new AlertDialog.Builder(RegisterActivity.this).setTitle("Alert").setMessage("Login already existing")
                                    .setPositiveButton("OK", null)
                                    .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);
                params.put("email", email);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                return params;
            }
        };
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }
}
