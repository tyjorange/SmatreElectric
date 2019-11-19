package com.rejuvee.smartelectric.family.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by liuchengran on 2018/12/4.
 */
public class RecordBean extends ViewModel implements Parcelable {
    public int state;// 告警TYPE ID
    public String desc;// 告警描述
    public int type;//0 :开关操作  1：场景操作
    public String name;
    public String time;
    public int cmdData;//1:合闸 0：拉闸
    public String code;
    public int source;
    public int runCode;//2:执行成功 1：执行失败  0：命令未发送
    public int runResult; //0:成功  其它失败，表示错误码
    public List<RecordBean> switchs;//type=1 时有该字段
    public String username;
    public String nickName;

    public RecordBean() {

    }

    private RecordBean(Parcel in) {
        state = in.readInt();
        desc = in.readString();
        type = in.readInt();
        name = in.readString();
        time = in.readString();
        cmdData = in.readInt();
        code = in.readString();
        source = in.readInt();
        runCode = in.readInt();
        runResult = in.readInt();
        switchs = in.createTypedArrayList(RecordBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(desc);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeInt(cmdData);
        dest.writeString(code);
        dest.writeInt(source);
        dest.writeInt(runCode);
        dest.writeInt(runResult);
        dest.writeTypedList(switchs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordBean> CREATOR = new Creator<RecordBean>() {
        @Override
        public RecordBean createFromParcel(Parcel in) {
            return new RecordBean(in);
        }

        @Override
        public RecordBean[] newArray(int size) {
            return new RecordBean[size];
        }
    };

}
