package com.buxingzhe.pedestrian.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by QJ on 2016/7/1.
 */
public class HotUserTag implements Parcelable{
    public Integer count;
    public Long id;
    public String tag;
    public boolean isSelect;
    public Long updateTime;
    public Long userId;

    public HotUserTag() {
    }

    protected HotUserTag(Parcel in) {
        tag = in.readString();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<HotUserTag> CREATOR = new Creator<HotUserTag>() {
        @Override
        public HotUserTag createFromParcel(Parcel in) {
            return new HotUserTag(in);
        }

        @Override
        public HotUserTag[] newArray(int size) {
            return new HotUserTag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
