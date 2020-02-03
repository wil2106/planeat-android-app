package com.planeat.front_end.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.planeat.front_end.R;
import com.planeat.front_end.api_service.AuthentificationService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Test").setTitle("Alert");
        AlertDialog dialog = builder.create();
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login){

            final String login = ((EditText)findViewById(R.id.editText_login)).getText().toString();
            final String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();


            if(login.trim().length()>0 && password.trim().length()>0){
                Toast.makeText(this,login+" "+password, Toast.LENGTH_LONG).show();
                AuthentificationService authService = new AuthentificationService(this);
                authService.login(login,password);
            }else{
                Toast.makeText(this,"fill all please", Toast.LENGTH_LONG).show();
                //Error message
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please fill in all the fields").setTitle("Alert");
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }


        }
        if(v.getId() == R.id.textView_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//if activity already in the stack, recover it and close activities on top of it
            startActivity(intent);
        }
    }
}
