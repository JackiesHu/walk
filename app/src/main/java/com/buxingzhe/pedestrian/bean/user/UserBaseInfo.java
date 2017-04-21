package com.buxingzhe.pedestrian.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 基本用户信息
 */
public class UserBaseInfo implements Parcelable{
    private String id;// userID
    private String avatarUrl;//头像
    private String nickName;//昵称
    private String gender;//性别

    public UserBaseInfo(){

    }

    protected UserBaseInfo(Parcel in) {
        id = in.readString();
        avatarUrl = in.readString();
        nickName = in.readString();
        gender = in.readString();
    }

    public static final Creator<UserBaseInfo> CREATOR = new Creator<UserBaseInfo>() {
        @Override
        public UserBaseInfo createFromParcel(Parcel in) {
            return new UserBaseInfo(in);
        }

        @Override
        public UserBaseInfo[] newArray(int size) {
            return new UserBaseInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(avatarUrl);
        dest.writeString(nickName);
        dest.writeString(gender);
    }
}
