package com.buxingzhe.pedestrian.bean.activity;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

import java.io.Serializable;

/**
 * Created by QJ on 2017/4/19.
 */

public class WalkRecordByActivity  implements Serializable {
    /**
     * id : 6c72f05fc7e543f7a950edc51196bdd0
     * createTime : 1492583455
     * user : {"id":"ac84f838f54c4122911b15459c808cce","nickName":"The has the","avatarUrl":"http://115.28.109.174:8282/image/2017032052320a.png"}
     * activity : 2b53345ffece4984a9eef018863867a2
     * stepCount : 21000
     * distance : 100
     * duration : 100
     * altitudeAsend : 100
     * altitudeHigh : 100
     * altitudeLow : 100
     * calorie : 100
     * fat : 100
     * nutrition : 100
     * introduction : 5
     * type : 0
     * title : 洪崖洞美食街
     * star : 5
     * streetStar : 2
     * envirStar : 2
     * safeStar : 2
     * state : 0
     * likeCount : 0
     * commentCount : 1
     * tags : 美不胜收;吃货的天堂;天天跑步
     * hasLike : 0
     * location : 321
     * views : http://115.28.109.174:8282/image/20170317ccb94c.png
     * routepicStr : http://115.28.109.174:8282/image/20170317ccb94c.png
     */

    private String id;
    private String createTime;
    private UserBaseInfo user;
    private String activity;
    private int stepCount;
    private int distance;
    private String duration;
    private String altitudeAsend;
    private String altitudeHigh;
    private String altitudeLow;
    private String calorie;
    private String fat;
    private String nutrition;
    private String introduction;
    private String type;
    private String title;
    private int star;
    private int streetStar;
    private int envirStar;
    private int safeStar;
    private String state;
    private int likeCount;
    private int commentCount;
    private String tags;
    private int hasLike;
    private String location;
    private String views;
    private String routepicStr;

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

    public UserBaseInfo getUser() {
        return user;
    }

    public void setUser(UserBaseInfo user) {
        this.user = user;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAltitudeAsend() {
        return altitudeAsend;
    }

    public void setAltitudeAsend(String altitudeAsend) {
        this.altitudeAsend = altitudeAsend;
    }

    public String getAltitudeHigh() {
        return altitudeHigh;
    }

    public void setAltitudeHigh(String altitudeHigh) {
        this.altitudeHigh = altitudeHigh;
    }

    public String getAltitudeLow() {
        return altitudeLow;
    }

    public void setAltitudeLow(String altitudeLow) {
        this.altitudeLow = altitudeLow;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
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

    public int getSafeStar() {
        return safeStar;
    }

    public void setSafeStar(int safeStar) {
        this.safeStar = safeStar;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getRoutepicStr() {
        return routepicStr;
    }

    public void setRoutepicStr(String routepicStr) {
        this.routepicStr = routepicStr;
    }
}