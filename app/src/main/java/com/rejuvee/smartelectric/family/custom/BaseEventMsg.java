package com.rejuvee.smartelectric.family.custom;

/**
 * Created by liuchengran on 2018/10/31.
 */

public class BaseEventMsg {
    public int eventType;
    public Object eventMsg;
    public boolean isSucess;
    public String errorMessage;
    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getEventMsg() {
        return eventMsg;
    }

    public void setEventMsg(Object eventMsg) {
        this.eventMsg = eventMsg;
    }

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean sucess) {
        isSucess = sucess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
