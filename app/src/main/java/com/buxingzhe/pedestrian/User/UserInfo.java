package com.buxingzhe.pedestrian.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.buxingzhe.pedestrian.bean.user.UserLoginResultInfo;
import com.buxingzhe.pedestrian.utils.SharedPreferencesUtil;

/**
 * 类描述：
 *
 * @author zhaishaoping
 * @data 11/04/2017 12:09 AM
 */
public class UserInfo {
    private String id;//userID
    private String createTimestamp;//创建时间
    private String avatar;//头像链接
    private String nickName;//昵称
    private String gender;//性别
    private String height;//身高cm
    private String weight;//体重kg
    private String unionId;//qq唯一标识
    private String openid;
    private String token;//访问凭证

    public UserInfo() {
    }
    public void formatUser(UserLoginResultInfo userLoginResultInfo){
        this.id = userLoginResultInfo.getId();
        this.avatar = userLoginResultInfo.getAvatarUrl();
        this.createTimestamp = userLoginResultInfo.getCreateTime();
        this.nickName = userLoginResultInfo.getNickName();
        this.gender = userLoginResultInfo.getGender();
        this.height = userLoginResultInfo.getHeight();
        this.weight = userLoginResultInfo.getWeight();
        this.unionId = userLoginResultInfo.getUnionId();
        this.openid = userLoginResultInfo.getOpenid();
        this.token = userLoginResultInfo.getToken();
    }

    /**
     * 保存数据
     * @param mContext
     * @param userInfo
     */
    public void saveUserInfo(Context mContext,UserInfo userInfo){
        SharedPreferencesUtil.getInstance().getSharedPreferences(mContext.getApplicationContext())
                .edit()
                .putString("token", userInfo.getToken())
                .putString("uid", userInfo.getId())
                .putString("avatar", userInfo.getAvatar())
                .putString("nickName", userInfo.getNickName())
                .putString("createTime",userInfo.getCreateTimestamp())
                .putString("gender",userInfo.getGender())
                .putString("height",userInfo.getHeight())
                .putString("weight",userInfo.getWeight())
                .putString("unionId",userInfo.getUnionId())
                .putString("openid",userInfo.getOpenid())
                .commit();
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 获取用户数据
     * @param mContext

     */
    public UserInfo getUserInfo(Context mContext){
        UserInfo userInfo = new UserInfo();
        SharedPreferences preferences = SharedPreferencesUtil.getInstance().getSharedPreferences(mContext.getApplicationContext());
        userInfo.setToken(preferences.getString("token",""));
        userInfo.setId(preferences.getString("uid", ""));
        userInfo.setAvatar(preferences.getString("avatar",""));
        userInfo.setNickName(preferences.getString("nickName", ""));
        userInfo.setCreateTimestamp(preferences.getString("createTime",""));
        userInfo.setGender(preferences.getString("gender",""));
        userInfo.setHeight(preferences.getString("height",""));
        userInfo.setWeight(preferences.getString("weight",""));
        userInfo.setUnionId(preferences.getString ("unionId",""));
        userInfo.setOpenid(preferences.getString("openid",""));
        return userInfo;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getUnionId() {
        return unionId;
    }

    public String getOpenid() {
        return openid;
    }

    public String getToken() {
        return token;
    }
}
