package com.example.eshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public NotificationsViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return text;
    }
}
