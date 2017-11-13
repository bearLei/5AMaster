package com.puti.education.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xjbin on 2017/5/8 0008.
 *
 * 个人佑证记录
 */

public class PersonProof implements Parcelable{

    public String title;
    public String type;
    public String address;
    public String involver;
    public String involverAvatar;
    public String time;

    public static final Parcelable.Creator<PersonProof> CREATOR = new Creator<PersonProof>() {
        public PersonProof createFromParcel(Parcel source) {
            PersonProof data = new PersonProof();

            data.title = source.readString();
            data.type = source.readString();
            data.address = source.readString();
            data.involver = source.readString();
            data.involverAvatar = source.readString();
            data.time = source.readString();

            return data;
        }

        public PersonProof[] newArray(int size) {
            return new PersonProof[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeString(involver);
        dest.writeString(involverAvatar);
        dest.writeString(time);
    }
}
