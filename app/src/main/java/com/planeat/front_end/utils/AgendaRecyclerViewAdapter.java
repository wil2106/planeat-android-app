package com.planeat.front_end.utils;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.planeat.front_end.R;

import java.util.List;

public class AgendaRecyclerViewAdapter extends RecyclerView.Adapter<AgendaRecyclerViewAdapter.ViewHolder> {

    private List<Pair<String, Integer>> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public AgendaRecyclerViewAdapter(Context context, List<Pair<String, Integer>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.agenda_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, Integer> agendaInfo = mData.get(position);
        holder.nameTV.setText(agendaInfo.first);
        holder.numberTV.setText("" + agendaInfo.second);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView numberTV;

        ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.agendaRecipeNameTextView);
            numberTV = itemView.findViewById(R.id.agendaRecipeNumberTextView);
        }
    }

}
