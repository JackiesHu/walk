package com.buxingzhe.pedestrian.bean.activity;

import java.io.Serializable;

/**
 * Created by QJ on 2017/4/13.
 */

public class WalkActivityInfo implements Serializable{
    /**
     * id : 8d26be4c2ea24ce797c52164f0778687
     * createTime : 1489579837
     * title : 11
     * introduction :
     * publisher : {"id":"e2f835b965e34b2fb8d6cbc22378f55a","nickName":"Apoptoxin","avatarUrl":"http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100"}
     * isOutDate : 1
     * attenderCount : 0
     * startTimeStamp : 1490099048
     * endTimestamp : 1490100600
     * banner : http://115.28.109.174:8282/image/201703154f9de2.png
     * createTimestamp : 1489579837
     */

    private String id;
    private String createTime;
    private String title;
    private String introduction;
    private PublisherBean publisher;
    private String isOutDate;
    private int attenderCount;
    private int startTimeStamp;
    private int endTimestamp;
    private String banner;
    private int createTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public PublisherBean getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherBean publisher) {
        this.publisher = publisher;
    }

    public String getIsOutDate() {
        return isOutDate;
    }

    public void setIsOutDate(String isOutDate) {
        this.isOutDate = isOutDate;
    }

    public int getAttenderCount() {
        return attenderCount;
    }

    public void setAttenderCount(int attenderCount) {
        this.attenderCount = attenderCount;
    }

    public int getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(int startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public int getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(int endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(int createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
