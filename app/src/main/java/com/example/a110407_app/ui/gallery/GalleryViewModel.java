package com.example.a110407_app.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();

        // if 資料庫撈日記資料 = NULL 才會顯示空空如也
        mText.setValue("目前空空如也喔 快來記錄你的生活吧");
    }

    public LiveData<String> getText() {
        return mText;
    }
}