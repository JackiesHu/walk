package com.buxingzhe.pedestrian.found.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

/**
 *
 * Created by hasee on 2017/4/25.
 */

public class WalkRecord extends RemarkPoint implements Parcelable{

    /**
     * id : 05595e723
     * isNewRecord : false
     * createTime : 2017-03-11 22:40:35
     * user : {"id":"b3d6aad59b5c4c0b83ae7b02d8618d66","isNewRecord":false,"avatar":"http://wekj.cn","nickName":"xiaoming"}
     * activity : 45454sf4sf3f1s
     * stepCount : 110
     * distance : 1245
     * duration : 100
     * altitudeAsend : 12
     * altitudeHigh : 456
     * altitudeLow : 132
     * calorie : 100
     * fat : 100
     * nutrition : 100
     * views : http://wekj.cn?1.png;http://wekj.cn?2.png
     * introduction : 很好玩
     * type : 0
     * routepicStr : http://wekj.cn?1.png
     * title : 不错的街道
     * star : 3
     * streetStar : 3
     * safeStar : 3
     * envirStar : 3
     * state : 0
     * likeCount : 2
     * commentCount : 2
     * hasLike : 0
     */

    private String id;
    private boolean isNewRecord;
    private String createTime;
    private UserBaseInfo user;
    private String activity;
    private int stepCount;
    private double distance;
    private String duration;
    private String altitudeAsend;
    private String altitudeHigh;
    private String altitudeLow;
    private String calorie;
    private String fat;
    private String nutrition;
    private String views;
    private String introduction;
    private String type;
    private String routepicStr;
    private String title;
    private String star;
    private int streetStar;
    private int safeStar;
    private int envirStar;
    private String state;
    private int likeCount;
    private int commentCount;
    private int hasLike;

    public WalkRecord(){

    }

    protected WalkRecord(Parcel in) {
        id = in.readString();
        isNewRecord = in.readByte() != 0;
        createTime = in.readString();
        user = in.readParcelable(UserBaseInfo.class.getClassLoader());
        activity = in.readString();
        stepCount = in.readInt();
        distance = in.readDouble();
        duration = in.readString();
        altitudeAsend = in.readString();
        altitudeHigh = in.readString();
        altitudeLow = in.readString();
        calorie = in.readString();
        fat = in.readString();
        nutrition = in.readString();
        views = in.readString();
        introduction = in.readString();
        type = in.readString();
        routepicStr = in.readString();
        title = in.readString();
        star = in.readString();
        streetStar = in.readInt();
        safeStar = in.readInt();
        envirStar = in.readInt();
        state = in.readString();
        likeCount = in.readInt();
        commentCount = in.readInt();
        hasLike = in.readInt();
    }

    public static final Creator<WalkRecord> CREATOR = new Creator<WalkRecord>() {
        @Override
        public WalkRecord createFromParcel(Parcel in) {
            return new WalkRecord(in);
        }

        @Override
        public WalkRecord[] newArray(int size) {
            return new WalkRecord[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
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

    public String getRoutepicStr() {
        return routepicStr;
    }

    public void setRoutepicStr(String routepicStr) {
        this.routepicStr = routepicStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getStreetStar() {
        return streetStar;
    }

    public void setStreetStar(int streetStar) {
        this.streetStar = streetStar;
    }

    public int getSafeStar() {
        return safeStar;
    }

    public void setSafeStar(int safeStar) {
        this.safeStar = safeStar;
    }

    public int getEnvirStar() {
        return envirStar;
    }

    public void setEnvirStar(int envirStar) {
        this.envirStar = envirStar;
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

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeByte((byte) (isNewRecord ? 1 : 0));
        dest.writeString(createTime);
        dest.writeParcelable(user, flags);
        dest.writeString(activity);
        dest.writeInt(stepCount);
        dest.writeDouble(distance);
        dest.writeString(duration);
        dest.writeString(altitudeAsend);
        dest.writeString(altitudeHigh);
        dest.writeString(altitudeLow);
        dest.writeString(calorie);
        dest.writeString(fat);
        dest.writeString(nutrition);
        dest.writeString(views);
        dest.writeString(introduction);
        dest.writeString(type);
        dest.writeString(routepicStr);
        dest.writeString(title);
        dest.writeString(star);
        dest.writeInt(streetStar);
        dest.writeInt(safeStar);
        dest.writeInt(envirStar);
        dest.writeString(state);
        dest.writeInt(likeCount);
        dest.writeInt(commentCount);
        dest.writeInt(hasLike);
    }
}
