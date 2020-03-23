package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

        if (v.getId() == R.id.btn_register){
            final String mail = ((EditText)findViewById(R.id.editText_mail)).getText().toString();
            final String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();


            if(mail.trim().length()>0 && password.trim().length()>0){
                register(mail, password);
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


    public void register(final String mail,final String password){

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        /* Show progress bar */
        progressBar.setVisibility(View.VISIBLE);

        String url = getString(R.string.server_url)+"/auth/registerUser";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                //if response status = 200
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        /*  Hide progress bar */
                        progressBar.setVisibility(View.INVISIBLE);

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//if activity already in the stack, recover it and close activities on top of it
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*  Hide progress bar */
                        progressBar.setVisibility(View.INVISIBLE);

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
                params.put("mail", mail);
                params.put("password", password);
                params.put("premium", "0");
                return params;
            }
        };
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }
}
