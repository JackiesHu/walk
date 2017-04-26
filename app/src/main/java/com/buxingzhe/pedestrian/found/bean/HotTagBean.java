package com.buxingzhe.pedestrian.found.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.buxingzhe.pedestrian.bean.HotUserTag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quanjing on 2017/2/27.
 */
public class HotTagBean implements Parcelable {
    public ArrayList<HotUserTag> hotSelectTags;

    public HotTagBean() {

    }


    protected HotTagBean(Parcel in) {
        hotSelectTags = in.createTypedArrayList(HotUserTag.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hotSelectTags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HotTagBean> CREATOR = new Creator<HotTagBean>() {
        @Override
        public HotTagBean createFromParcel(Parcel in) {
            return new HotTagBean(in);
        }

        @Override
        public HotTagBean[] newArray(int size) {
            return new HotTagBean[size];
        }
    };
}
