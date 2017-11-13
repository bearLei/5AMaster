package com.puti.education.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xjbin on 2017/5/16 0016.
 */

public class ImageRecord implements Parcelable{

    public String time;
    public String desc;
    public List<String> ImageList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(desc);
        dest.writeList(ImageList);
    }

    public static final Parcelable.Creator<ImageRecord> CREATOR = new Creator<ImageRecord>() {
        public ImageRecord createFromParcel(Parcel source) {
            ImageRecord record = new ImageRecord();
            record.time = source.readString();
            record.desc = source.readString();
            source.readList(record.ImageList,String.class.getClassLoader());
            return record;
        }

        public ImageRecord[] newArray(int size) {
            return new ImageRecord[size];
        }

    };
}
