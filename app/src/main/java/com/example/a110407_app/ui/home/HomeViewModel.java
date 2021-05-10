package com.example.a110407_app.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("目前沒有最新的日記唷！快點來寫日記吧！");
    }

    public LiveData<String> getText() {
        return mText;
    }
}