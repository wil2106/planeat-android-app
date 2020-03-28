package com.planeat.front_end.mainactivity_fragments.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.planeat.front_end.R;
import com.planeat.front_end.utils.AgendaRecyclerViewAdapter;
import com.planeat.front_end.utils.NetworkSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AgendaFragment extends Fragment {

    private AgendaViewModel agendaViewModel;
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
        agendaViewModel =
                ViewModelProviders.of(this).get(AgendaViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_agenda, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_agenda);
        agendaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        fillDates(root);
        Button btnPrevious = root.findViewById(R.id.buttonAgendaPrevious);
        Button btnNext = root.findViewById(R.id.buttonAgendaNext);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(-7);
                fillDates(root);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(7);
                fillDates(root);
            }
        });
        LinearLayout layoutDate2 = root.findViewById(R.id.dateLayout2);
        LinearLayout layoutDate3 = root.findViewById(R.id.dateLayout3);
        LinearLayout layoutDate4 = root.findViewById(R.id.dateLayout4);
        LinearLayout layoutDate5 = root.findViewById(R.id.dateLayout5);
        LinearLayout layoutDate6 = root.findViewById(R.id.dateLayout6);
        LinearLayout layoutDate7 = root.findViewById(R.id.dateLayout7);
        layoutDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(1);
                fillDates(root);
            }
        });
        layoutDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(2);
                fillDates(root);
            }
        });
        layoutDate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(3);
                fillDates(root);
            }
        });
        layoutDate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(4);
                fillDates(root);
            }
        });
        layoutDate6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(5);
                fillDates(root);
            }
        });
        layoutDate7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSingleton.getInstance(activity).offsetAgendaDatesRange(6);
                fillDates(root);
            }
        });
        fillRecipes(root);
        return root;
    }

    private void fillDates(View root) { //fills the date textviews according to the current date
        TextView dayTV1 = root.findViewById(R.id.dayTextView1);
        TextView dayNumTV1 = root.findViewById(R.id.dayNumTextView1);
        TextView dayTV2 = root.findViewById(R.id.dayTextView2);
        TextView dayNumTV2 = root.findViewById(R.id.dayNumTextView2);
        TextView dayTV3 = root.findViewById(R.id.dayTextView3);
        TextView dayNumTV3 = root.findViewById(R.id.dayNumTextView3);
        TextView dayTV4 = root.findViewById(R.id.dayTextView4);
        TextView dayNumTV4 = root.findViewById(R.id.dayNumTextView4);
        TextView dayTV5 = root.findViewById(R.id.dayTextView5);
        TextView dayNumTV5 = root.findViewById(R.id.dayNumTextView5);
        TextView dayTV6 = root.findViewById(R.id.dayTextView6);
        TextView dayNumTV6 = root.findViewById(R.id.dayNumTextView6);
        TextView dayTV7 = root.findViewById(R.id.dayTextView7);
        TextView dayNumTV7 = root.findViewById(R.id.dayNumTextView7);
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, NetworkSingleton.getInstance(activity).getAgendaDatesRange());
        Date d = calendar.getTime();
        dayTV1.setText(sdf.format(d));
        SimpleDateFormat sdfNum = new SimpleDateFormat("dd");
        dayNumTV1.setText(sdfNum.format(d));
        TextView completeDateTV = root.findViewById(R.id.currentDayTextView);
        completeDateTV.setText(DateFormat.format("EEEE", d) + " " + sdfNum.format(d) + "th " + DateFormat.format("MMMM", d));
        NetworkSingleton.getInstance(activity).setAgendaCurrentDay(sdfNum.format(d));
        NetworkSingleton.getInstance(activity).setAgendaCurrentMonth(DateFormat.format("MM", d).toString());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV2.setText(sdf.format(d));
        dayNumTV2.setText(sdfNum.format(d));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV3.setText(sdf.format(d));
        dayNumTV3.setText(sdfNum.format(d));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV4.setText(sdf.format(d));
        dayNumTV4.setText(sdfNum.format(d));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV5.setText(sdf.format(d));
        dayNumTV5.setText(sdfNum.format(d));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV6.setText(sdf.format(d));
        dayNumTV6.setText(sdfNum.format(d));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        dayTV7.setText(sdf.format(d));
        dayNumTV7.setText(sdfNum.format(d));
    }

    private void fillRecipes(View root) {
        String url = getString(R.string.server_url) + "/users/profile";
        /* Get access token from shared preferences */
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_prefs", MODE_PRIVATE );
        token = sharedPreferences.getString("access_token", null);
        JsonArrayRequest profileGetRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", response.getJSONObject(0).getInt("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("meh", "failed to get user_id from server");
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
        final ArrayList<Pair<String, Integer>> breakfastInfos = new ArrayList<>(); // we fill the arraylist that will be used by the recyclerview containing the info
        final ArrayList<Pair<String, Integer>> lunchInfos = new ArrayList<>();
        final ArrayList<Pair<String, Integer>> dinnerInfos = new ArrayList<>();
        /*String url2 = getString(R.string.server_url) + "/planning";
        JsonArrayRequest planningGetRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    for (int i = 0; i < response.length(); ++i) {
                        date = format.parse(response.getJSONObject(i).getJSONArray("planning").getJSONObject(0).getString("date"));
                        if (DateFormat.format("MM", date).toString().equals(NetworkSingleton.getInstance(activity).getAgendaCurrentMonth()) &&
                                DateFormat.format("dd", date).equals(NetworkSingleton.getInstance(activity).getAgendaCurrentDay())) {
                            String mealType = response.getJSONObject(i).getJSONArray("planning").getJSONObject(0).getJSONObject("meal_type").getString("name");
                            Pair<String, Integer> mealInfo = new Pair<>(response.getJSONObject(i).getString("recipe_name"),
                                                                        response.getJSONObject(i).getInt("recipe_nb_servings")); // example filling
                            switch (mealType) {
                                case "breakfast":
                                    breakfastInfos.add(mealInfo);
                                    break;
                                case "lunch":
                                    lunchInfos.add(mealInfo);
                                    break;
                                case "dinner":
                                    dinnerInfos.add(mealInfo);
                                    break;
                                default:
                                    Log.i("meh", "One meal from the planning had an invalid meal_type");
                            }
                        }
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("meh", "failed to get user_id from server");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", "" + sharedPreferences.getInt("user_id", -1));
                return params;
            }
        };
        NetworkSingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(profileGetRequest);*/
        Pair<String, Integer> breakfastInfo = new Pair<>("pancakes", 1); // example filling
        breakfastInfos.add(breakfastInfo);
        Pair<String, Integer> breakfastInfo2 = new Pair<>("smoothie", 1);
        breakfastInfos.add(breakfastInfo2);
        Pair<String, Integer> lunchInfo = new Pair<>("Poulet au boulgour", 4);
        lunchInfos.add(lunchInfo);
        Pair<String, Integer> dinnerInfo = new Pair<>("crosets aux lardons, cheddar et oignons", 2);
        dinnerInfos.add(dinnerInfo);
        Pair<String, Integer> dinnerInfo2 = new Pair<>("Verrines parfumée à la violette", 4);
        dinnerInfos.add(dinnerInfo2);
        RecyclerView breakfastRecyclerView = (RecyclerView) root.findViewById(R.id.breakfastRecyclerView);
        RecyclerView lunchRecyclerView = (RecyclerView) root.findViewById(R.id.lunchRecyclerView);
        RecyclerView dinnerRecyclerView = (RecyclerView) root.findViewById(R.id.dinnerRecyclerView);
        breakfastRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        AgendaRecyclerViewAdapter adapter = new AgendaRecyclerViewAdapter(activity, breakfastInfos);
        breakfastRecyclerView.setAdapter(adapter);
        lunchRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AgendaRecyclerViewAdapter(activity, lunchInfos);
        lunchRecyclerView.setAdapter(adapter);
        dinnerRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AgendaRecyclerViewAdapter(activity, dinnerInfos);
        dinnerRecyclerView.setAdapter(adapter);
    }
}