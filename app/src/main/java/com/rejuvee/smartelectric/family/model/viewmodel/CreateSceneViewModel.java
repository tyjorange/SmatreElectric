package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateSceneViewModel extends ViewModel {
    private MutableLiveData<String> sceneName;

    public MutableLiveData<String> getSceneName() {
        if (sceneName == null) {
            sceneName = new MutableLiveData<>();
        }
        return sceneName;
    }

    public void setSceneName(String _sceneName) {
        if (sceneName == null) {
            sceneName = new MutableLiveData<>();
        }
        sceneName.setValue(_sceneName);
    }
}
