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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.dao.NetworkSingleton;

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
            EditText editText_login = (EditText)findViewById(R.id.editText_login);
            EditText editText_password = (EditText)findViewById(R.id.editText_password);

            final String login = editText_login.getText().toString();
            final String password = editText_password.getText().toString();

            if(login.isEmpty() || password.isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                builder.setMessage("Please fill in all the fields")
                        .setTitle("Alert");
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }else{
                //if all fields are filled, make the post
                final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Checking credentials. Please wait...", true);
                String url = getString(R.string.server_url)+"/login";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                JSONObject responseObject = null;
                                try {
                                    responseObject = new JSONObject(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
                                builder.setMessage("Invalid credentials")
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
                        return params;
                    }
                };
                NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
            }
        }
        if(v.getId() == R.id.textView_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
