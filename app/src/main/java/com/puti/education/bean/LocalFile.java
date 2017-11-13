package com.puti.education.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Administrator on 2017/5/23 0023.
 *
 */

public class LocalFile implements Parcelable{

    public String fileName;
    public String size;
    public String localPath;
    public int isCheck;//0未选中1选中
    public long modifyTime;

    public LocalFile(String fileName, String size,String localPath, long time) {
        this.fileName = fileName;
        this.size = size;
        this.localPath = localPath;
        this.modifyTime= time;
    }

    public LocalFile() {
    }

    public static final Parcelable.Creator<LocalFile> CREATOR = new Parcelable.Creator<LocalFile>() {
        public LocalFile createFromParcel(Parcel source) {
            LocalFile localFile = new LocalFile();
            localFile.fileName = source.readString();
            localFile.size = source.readString();
            localFile.localPath = source.readString();
            localFile.isCheck = source.readInt();
            localFile.modifyTime = source.readLong();
            return localFile;
        }

        public LocalFile[] newArray(int size) {
            return new LocalFile[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(size);
        dest.writeString(localPath);
        dest.writeInt(isCheck);
        dest.writeLong(modifyTime);
    }

}
