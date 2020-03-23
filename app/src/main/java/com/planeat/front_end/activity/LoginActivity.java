package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

            /* Retrieve layout objects */
            final String mail = ((EditText)findViewById(R.id.editText_mail)).getText().toString();
            final String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();

            /* Check if not empty */
            if(mail.trim().length()>0 && password.trim().length()>0){
                login(mail,password);
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


    public void login(final String mail,final String password){


        final ProgressBar progressBar = findViewById(R.id.progressBar);
        /* Show progress bar */
        progressBar.setVisibility(View.VISIBLE);

        String url = getString(R.string.server_url)+"/auth/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObject = null;
                        try {
                            responseObject = new JSONObject(response);

                            /* Store access token in shared preferences */
                            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token",responseObject.getString("access_token"));
                            editor.apply();

                            /*  Hide progress bar */
                            progressBar.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();//remove activity from the stack
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /* IF we want to persist credentials :
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                                */
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*  Hide progress bar */
                        progressBar.setVisibility(View.INVISIBLE);
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
                params.put("username", mail);
                params.put("password", password);
                params.put("grant_type","password");
                params.put("client_id","test");
                params.put("client_secret","test");
                return params;
            }
        };
        NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }


    /* IF we want to persist credentials :
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE );
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token",responseObject.getString("access_token"));
                    editor.apply();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };*/

}
