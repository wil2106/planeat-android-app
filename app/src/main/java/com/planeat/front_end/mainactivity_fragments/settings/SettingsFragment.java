package com.planeat.front_end.mainactivity_fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.planeat.front_end.R;
import com.planeat.front_end.activity.LoginActivity;
import com.planeat.front_end.activity.PremiumSubActivity;
import com.planeat.front_end.activity.ProfileActivity;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity =(Activity) context; // here, we get the instance of the current activity to use its functions
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        /*final TextView textView = root.findViewById(R.id.text_settings);
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        Switch notifications = (Switch) root.findViewById(R.id.switchNotifications);
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // HERE WE NEED TO PUT A BOOLEAN EQUAL TO isChecked FOR NOTIFICATIONS ALLOWED OR NOT AS PERSISTENT DATA, the isChecked will be
                // true if the switch is in the On position
            }
        });
        Button profile = (Button) root.findViewById(R.id.buttonProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(activity, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
        Button disconnect = (Button) root.findViewById(R.id.buttonDisconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HERE WE SHOULD PROPERLY LOGOUT (AS IN: SEND A LOGOUT POST REQUEST)
                Intent disconnectIntent = new Intent(activity, LoginActivity.class);
                activity.finish();
                startActivity(disconnectIntent);
            }
        });
        Button premiumSub = (Button) root.findViewById(R.id.buttonSettingsPremium);
        // premiumSub.setVisibility(View.GONE); DO THIS IF USER IS ALREADY PREMIUM, ELSE THE NEXT BLOCK
        premiumSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent premiumIntent = new Intent(activity, PremiumSubActivity.class);
                startActivity(premiumIntent);
            }
        });
        return root;
    }
}