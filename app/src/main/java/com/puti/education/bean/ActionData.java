package com.puti.education.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xjbin on 2017/5/8 0008.
 *
 * 行为数据
 */

public class ActionData implements Parcelable{

    public String name;
    public double value;

    public static final Parcelable.Creator<ActionData> CREATOR = new Creator<ActionData>() {
        public ActionData createFromParcel(Parcel source) {
            ActionData data = new ActionData();

            data.value = source.readDouble();
            data.name = source.readString();
            return data;
        }

        public ActionData[] newArray(int size) {
            return new ActionData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(value);

    }
}
