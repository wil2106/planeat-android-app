package com.planeat.front_end.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.planeat.front_end.R;
import com.planeat.front_end.mainactivity_fragments.settings.SettingsFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button returnBtn = (Button) findViewById(R.id.buttonProfileReturn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(returnIntent);
            }
        });
    }

}
