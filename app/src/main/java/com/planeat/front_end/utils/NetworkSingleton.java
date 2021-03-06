package com.planeat.front_end.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class NetworkSingleton {
    private static NetworkSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private static int agendaDatesRange = 0;
    private static String agendaCurrentDay = "00";
    private static String agendaCurrentMonth = "00";
    private static String agendaCurrentYear = "0000";
    private static int user_id = -1;

    private NetworkSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public void offsetAgendaDatesRange(int offset) {
        agendaDatesRange += offset;
    }

    public int getAgendaDatesRange() {
        return agendaDatesRange;
    }

    public void setAgendaCurrentDay(String day) {
        agendaCurrentDay = day;
    }

    public String getAgendaCurrentDay() {
        return agendaCurrentDay;
    }

    public void setAgendaCurrentMonth(String month) {
        agendaCurrentMonth = month;
    }

    public String getAgendaCurrentMonth() {
        return agendaCurrentMonth;
    }

    public void setAgendaCurrentYear(String year) {
        agendaCurrentYear = year;
    }

    public String getAgendaCurrentYear() {
        return agendaCurrentYear;
    }

    public void setUserId(int id) { this.user_id = id; }

    public int getUserId() { return this.user_id; }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}