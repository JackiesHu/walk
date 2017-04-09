package com.buxingzhe.pedestrian.bean.user;

/**
 * 增加用户步票
 * Created by zhaishaoping on 27/03/2017.
 */
public class UserAddWalkNumInfo {
    private String id;//userID
    private String createTime;//创建时间
    private String avatar;//头像
    private String nickName;//昵称
    private String gender;//性别
    private String height;//身高cm
    private String weight;//体重kg
    private String score;//积分
    private String walkMoney;//步票
    private String unionId;//qq唯一标识
    private String registerType;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWalkMoney() {
        return walkMoney;
    }

    public void setWalkMoney(String walkMoney) {
        this.walkMoney = walkMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }
}
