package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTopicViewModel extends ViewModel {
    private MutableLiveData<String> topic;
    private MutableLiveData<String> context;

    public MutableLiveData<String> getTopic() {
        if (topic == null) {
            topic = new MutableLiveData<>();
        }
        return topic;
    }

    public void setTopic(String _topic) {
        if (topic == null) {
            topic = new MutableLiveData<>();
        }
        topic.setValue(_topic);
    }

    public MutableLiveData<String> getContext() {
        if (context == null) {
            context = new MutableLiveData<>();
        }
        return context;
    }

    public void setContext(String _context) {
        if (context == null) {
            context = new MutableLiveData<>();
        }
        context.setValue(_context);
    }
}
