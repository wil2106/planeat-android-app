package com.planeat.front_end.mainactivity_fragments.agenda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AgendaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AgendaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to the agenda");
    }

    public LiveData<String> getText() {
        return mText;
    }
}