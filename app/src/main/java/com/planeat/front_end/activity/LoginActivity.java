package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.api_access.NetworkSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login){

            final String login = ((EditText)findViewById(R.id.editText_login)).getText().toString();
            final String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();


            if(login.trim().length()>0 && password.trim().length()>0){
                login(login,password);
            }else{
                //Error message
                new AlertDialog.Builder(LoginActivity.this).setTitle("Alert").setMessage("Please fill in all required fields")
                        .setPositiveButton("OK", null)
                        .show();
            }

        }
        if(v.getId() == R.id.textView_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//if activity already in the stack, recover it and close activities on top of it
            startActivity(intent);
        }
    }


    public void login(final String login,final String password){

        String url = getString(R.string.server_url)+"/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObject = null;
                        try {
                            responseObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),responseObject.toString(), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();//remove activity from the stack
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(LoginActivity.this).setTitle("Alert").setMessage("Login or password is incorrect, please retry")
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
                return params;
            }
        };
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }
}
