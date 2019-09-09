package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by liuchengran on 2017/12/15.
 * <p>
 * 场景  一个场景包含个断路器(线路)
 */

public class SceneBean extends RealmObject implements Parcelable {

    @SerializedName("name")
    private String sceneName;//场景名称
    @PrimaryKey
    @SerializedName("sceneID")
    private String sceneId;

    @SerializedName("iconType")
    private int sceneIconRes;
    //  private RealmList<SwitchBean> listBreak;
    private String collectorID;
    private String count;

    @Ignore
    public int showDelIcon = View.GONE;

    public SceneBean() {

    }

    protected SceneBean(Parcel in) {
        sceneName = in.readString();
        sceneId = in.readString();
        sceneIconRes = in.readInt();
        collectorID = in.readString();
        count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sceneName);
        dest.writeString(sceneId);
        dest.writeInt(sceneIconRes);
        dest.writeString(collectorID);
        dest.writeString(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<SceneBean> CREATOR = new Creator<SceneBean>() {
        @Override
        public SceneBean createFromParcel(Parcel in) {
            return new SceneBean(in);
        }

        @Override
        public SceneBean[] newArray(int size) {
            return new SceneBean[size];
        }
    };

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public int getSceneIconRes() {
        return sceneIconRes;
    }

    public void setSceneIconRes(int sceneIconRes) {
        this.sceneIconRes = sceneIconRes;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
    /* public int getListBreaksize() {
        return listBreaksize;
    }

    public void setListBreaksize(int listBreaksize) {
        this.listBreaksize = listBreaksize;
    }*/
    /* public RealmList<SwitchBean> getListBreak() {
        return listBreak;
    }

    public void setListBreak(RealmList<SwitchBean> listBreak) {
        this.listBreak = listBreak;
    }

    public List<SwitchBean> getJavaListBreak() {
        List<SwitchBean> listData = new ArrayList<>();
        for (SwitchBean breaker : listBreak) {
            listData.add(new SwitchBean(breaker));
        }

        return listData;
    }*/
}
