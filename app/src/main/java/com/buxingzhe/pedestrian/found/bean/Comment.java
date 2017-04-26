package com.buxingzhe.pedestrian.found.bean;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

/**
 * Created by hasee on 2017/4/25.
 */

public class Comment {

    private String id;
    private long createTime;
    private UserBaseInfo user;
    private String remarkPoint;
    private int safeStar;
    private int streetStar;
    private int envirStar;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public void setUser(UserBaseInfo user) {
        this.user = user;
    }

    public String getRemarkPoint() {
        return remarkPoint;
    }

    public void setRemarkPoint(String remarkPoint) {
        this.remarkPoint = remarkPoint;
    }

    public int getSafeStar() {
        return safeStar;
    }

    public void setSafeStar(int safeStar) {
        this.safeStar = safeStar;
    }

    public int getStreetStar() {
        return streetStar;
    }

    public void setStreetStar(int streetStar) {
        this.streetStar = streetStar;
    }

    public int getEnvirStar() {
        return envirStar;
    }

    public void setEnvirStar(int envirStar) {
        this.envirStar = envirStar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
