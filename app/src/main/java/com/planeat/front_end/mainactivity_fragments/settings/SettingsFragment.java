package com.planeat.front_end.mainactivity_fragments.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.activity.LoginActivity;
import com.planeat.front_end.activity.PremiumSubActivity;
import com.planeat.front_end.activity.ProfileActivity;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    Activity activity;
    String token;

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
        final TextView accountNameTV = (TextView) root.findViewById(R.id.settingsAccountTextView);
        String url = getString(R.string.server_url) + "/users/profile";
        /* Get access token from shared preferences */
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);
        final Button premiumSub = (Button) root.findViewById(R.id.buttonSettingsPremium);
        JsonArrayRequest profileGetRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    int indexMailChar = -1;
                    String user_mail = response.getJSONObject(0).getString("user_mail");
                    indexMailChar = user_mail.indexOf('.');
                    if (indexMailChar != -1) {
                        user_mail = (char)(user_mail.charAt(0) - 32) + user_mail.substring(1, indexMailChar) + " " + (char)(user_mail.charAt(indexMailChar + 1) - 32) + "."; // error if mail ends with '.', but clearly shouldn't happen
                    }
                    else {
                        indexMailChar = user_mail.indexOf('@');
                        if (indexMailChar != -1) {
                            user_mail = user_mail.substring(0, indexMailChar);
                        }
                    }
                    accountNameTV.setText(user_mail);
                    if (response.getJSONObject(0).getInt("user_ispremium") != 0)
                        premiumSub.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                accountNameTV.setText("User");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        NetworkSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(profileGetRequest);

        Switch notifications = (Switch) root.findViewById(R.id.switchNotifications);
        notifications.setChecked(sharedPreferences.getBoolean("notifications", false));
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // HERE WE NEED TO PUT A BOOLEAN EQUAL TO isChecked FOR NOTIFICATIONS ALLOWED OR NOT AS PERSISTENT DATA, the isChecked will be
                // true if the switch is in the On position
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications",isChecked);
                editor.apply();
            }
        });

        RelativeLayout profile = (RelativeLayout) root.findViewById(R.id.buttonProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(activity, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        RelativeLayout disconnect = (RelativeLayout) root.findViewById(R.id.buttonDisconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HERE WE SHOULD PROPERLY LOGOUT (AS IN: SEND A LOGOUT POST REQUEST)
                Intent disconnectIntent = new Intent(activity, LoginActivity.class);
                activity.finish();
                startActivity(disconnectIntent);
            }
        });

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