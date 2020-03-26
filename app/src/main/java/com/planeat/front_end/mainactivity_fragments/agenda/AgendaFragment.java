package com.planeat.front_end.mainactivity_fragments.agenda;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.planeat.front_end.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgendaFragment extends Fragment {

    private AgendaViewModel agendaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agendaViewModel =
                ViewModelProviders.of(this).get(AgendaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agenda, container, false);
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
        Date d = calendar.getTime();
        dayTV1.setText(sdf.format(d));
        SimpleDateFormat sdfNum = new SimpleDateFormat("dd");
        dayNumTV1.setText(sdfNum.format(d));
        TextView completeDateTV = root.findViewById(R.id.currentDayTextView);
        completeDateTV.setText(DateFormat.format("EEEE", d) + " " + sdfNum.format(d) + "th " + DateFormat.format("MMMM", d));
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
}