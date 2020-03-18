package com.planeat.front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.planeat.front_end.R;

public class PremiumSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_sub);
        Button cancelBtn = (Button) findViewById(R.id.buttonPremiumCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(PremiumSubActivity.this, MainActivity.class);
                startActivity(returnIntent);
            }
        });
    }

}
