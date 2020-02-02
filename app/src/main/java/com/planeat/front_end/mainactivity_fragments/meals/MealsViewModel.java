package com.planeat.front_end.mainactivity_fragments.meals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MealsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to the meals");
    }

    public LiveData<String> getText() {
        return mText;
    }
}