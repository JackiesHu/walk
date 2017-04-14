package com.buxingzhe.pedestrian.bean.user;

/**
 * 基本用户信息
 */
public class UserBaseInfo {
    private String id;// userID
    private String avatarUrl;//头像
    private String nickName;//昵称
    private String gender;//性别

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
}
