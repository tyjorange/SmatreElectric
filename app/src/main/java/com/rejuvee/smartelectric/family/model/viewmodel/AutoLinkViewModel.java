package com.rejuvee.smartelectric.family.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AutoLinkViewModel extends ViewModel {
    private MutableLiveData<String> ssid;
    private MutableLiveData<String> pasd;
    private MutableLiveData<String> port;

    public MutableLiveData<String> getSsid() {
        if (ssid == null) {
            ssid = new MutableLiveData<>();
        }
        return ssid;
    }

    public void setSsid(String _ssid) {
        if (ssid == null) {
            ssid = new MutableLiveData<>();
        }
        ssid.setValue(_ssid);
    }

    public MutableLiveData<String> getPasd() {
        if (pasd == null) {
            pasd = new MutableLiveData<>();
        }
        return pasd;
    }

    public void setPasd(String _pasd) {
        if (pasd == null) {
            pasd = new MutableLiveData<>();
        }
        pasd.setValue(_pasd);
    }

    public MutableLiveData<String> getPort() {
        if (port == null) {
            port = new MutableLiveData<>();
        }
        return port;
    }

    public void setPort(String _port) {
        if (port == null) {
            port = new MutableLiveData<>();
        }
        port.setValue(_port);
    }
}
