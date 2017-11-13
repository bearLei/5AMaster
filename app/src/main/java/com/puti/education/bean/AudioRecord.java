package com.puti.education.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xjbin on 2017/5/16 0016.
 */

public class AudioRecord implements Parcelable {

    public String time;
    public List<String> audioList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeList(audioList);
    }

    public static final Parcelable.Creator<AudioRecord> CREATOR = new Parcelable.Creator<AudioRecord>() {
        public AudioRecord createFromParcel(Parcel source) {
            AudioRecord record = new AudioRecord();
            record.time = source.readString();
            source.readList(record.audioList,String.class.getClassLoader());
            return record;
        }

        public AudioRecord[] newArray(int size) {
            return new AudioRecord[size];
        }

    };
}
