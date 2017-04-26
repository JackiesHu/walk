package com.buxingzhe.pedestrian.bean.user;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class User implements Serializable {

    String id;
    String nickname;
    String thirdType;
    String thirdId;
    String header;
    String sex;
    String age;   // 默认为20
    String height;  // 默认
    String weight;  // 默认为60kg
    int intg;  // 积分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }

    public int getIntg() {
        return intg;
    }

    public void setIntg(int intg) {
        this.intg = intg;
    }
}
