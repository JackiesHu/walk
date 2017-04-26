package com.buxingzhe.pedestrian.bean.activity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by QJ on 2017/4/17.
 */

public class PublisherBean implements Parcelable{
    /**
     * id : e2f835b965e34b2fb8d6cbc22378f55a
     * nickName : Apoptoxin
     * avatarUrl : http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100
     */

    private String id;
    private String nickName;
    private String avatarUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
