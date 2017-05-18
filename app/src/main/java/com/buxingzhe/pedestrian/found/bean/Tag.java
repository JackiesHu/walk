package com.buxingzhe.pedestrian.found.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hasee on 2017/4/25.
 */

public class Tag implements Parcelable{

    /**
     * id : 11c62f09a0a045fe9f01c919ca67e60a
     * name : aaa
     * code : 0
     * userId : 123123123
     */

    private String id;
    private String name;
    private String code;
    private String userId;
    private boolean isSelect;

    protected Tag(Parcel in) {
        id = in.readString();
        name = in.readString();
        code = in.readString();
        userId = in.readString();
        isSelect = in.readByte() != 0;
    }

    public Tag(){

    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(userId);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
