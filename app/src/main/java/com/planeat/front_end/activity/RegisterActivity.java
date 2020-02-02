package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.dao.NetworkSingleton;

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
            final String firstName = ((EditText)findViewById(R.id.editText_firstName)).getText().toString();
            final String lastName = ((EditText)findViewById(R.id.editText_lastName)).getText().toString();

            if(login.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                builder.setMessage("Please fill in all the fields")
                        .setTitle("Alert");
                AlertDialog dialog = builder.create();
            }else{
                //if all fields are filled, make the post
                final ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this, "",
                        "Checking credentials. Please wait...", true);
                String url = getString(R.string.server_url)+"/register";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                                builder.setMessage("Registered successfully !")
                                        .setTitle("Info");
                                AlertDialog dialog = builder.create();
                                dialog.dismiss();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                                builder.setMessage("Registered successfully !")
                                        .setTitle("Alert");
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
                        params.put("email", email);
                        params.put("firstName", firstName);
                        params.put("lastName", lastName);
                        return params;
                    }
                };
                NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
            }
        }


        if(v.getId() == R.id.textView_login){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
