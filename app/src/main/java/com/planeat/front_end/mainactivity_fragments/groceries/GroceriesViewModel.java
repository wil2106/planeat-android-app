package com.planeat.front_end.mainactivity_fragments.groceries;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroceriesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GroceriesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to the groceries");
    }

    public LiveData<String> getText() {
        return mText;
    }
}