package com.buxingzhe.pedestrian.bean.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

/**
 * Created by QJ on 2017/4/14.
 */

public class WalkRecordInfo implements Parcelable {
    /**
     * id : 439b419a722444b98ec49dbf43d78024
     * createTime : 1491443919
     * user : {"id":"a94efac9babb4c5785bac5735724dec4","nickName":"Mi","avatarUrl":"http://115.28.109.174:8282/image/20170401f50537.png"}
     * stepCount : 0
     * distance : 0
     * duration : 13
     * altitudeAsend : 0
     * altitudeHigh : 54.94991302490234
     * altitudeLow : 54.94991302490234
     * calorie : 0.5305389165878296
     * fat : 90
     * nutrition : 30
     * introduction :
     * type : 0
     * star : 0
     * streetStar : 0
     * envirStar : 0
     * safeStar : 0
     * state : 0
     * likeCount : 1
     * commentCount : 0
     * hasLike : 0
     * path : [
     {
     "longitude" : 116.36286939066,
     "latitude" : 39.965619797845
     },
     {
     "longitude" : 116.36286939066,
     "latitude" : 39.965619797845
     }
     ]
     * location : 北京市海淀区西土城路10号院-14号楼
     * routepicStr : http://115.28.109.174:8282/image/2017040675f22e.png
     * activity : 6acedc886fbb40f3b21569646ae9f228
     * views : http://115.28.109.174:8282/image/201703292920d0.png
     * title : 11
     * tags : 美不胜收;吃货的天堂;天天跑步
     */

    private String id;
    private String createTime;
    private UserBaseInfo user;
    private int stepCount;
    private double distance;
    private String duration;
    private String altitudeAsend;
    private String altitudeHigh;
    private String altitudeLow;
    private String calorie;
    private String fat;
    private String nutrition;
    private String introduction;
    private String type;
    private int star;
    private int streetStar;
    private int envirStar;
    private int safeStar;
    private String state;
    private int likeCount;
    private int commentCount;
    private int hasLike;
    private String path;
    private String location;
    private String routepicStr;
    private String activity;
    private String views;
    private String title;
    private String tags;

    protected WalkRecordInfo(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        user = in.readParcelable(UserBaseInfo.class.getClassLoader());
        stepCount = in.readInt();
        distance = in.readDouble();
        duration = in.readString();
        altitudeAsend = in.readString();
        altitudeHigh = in.readString();
        altitudeLow = in.readString();
        calorie = in.readString();
        fat = in.readString();
        nutrition = in.readString();
        introduction = in.readString();
        type = in.readString();
        star = in.readInt();
        streetStar = in.readInt();
        envirStar = in.readInt();
        safeStar = in.readInt();
        state = in.readString();
        likeCount = in.readInt();
        commentCount = in.readInt();
        hasLike = in.readInt();
        path = in.readString();
        location = in.readString();
        routepicStr = in.readString();
        activity = in.readString();
        views = in.readString();
        title = in.readString();
        tags = in.readString();
    }

    public static final Creator<WalkRecordInfo> CREATOR = new Creator<WalkRecordInfo>() {
        @Override
        public WalkRecordInfo createFromParcel(Parcel in) {
            return new WalkRecordInfo(in);
        }

        @Override
        public WalkRecordInfo[] newArray(int size) {
            return new WalkRecordInfo[size];
        }
    };

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

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoutepicStr() {
        return routepicStr;
    }

    public void setRoutepicStr(String routepicStr) {
        this.routepicStr = routepicStr;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(createTime);
        parcel.writeParcelable(user, i);
        parcel.writeInt(stepCount);
        parcel.writeDouble(distance);
        parcel.writeString(duration);
        parcel.writeString(altitudeAsend);
        parcel.writeString(altitudeHigh);
        parcel.writeString(altitudeLow);
        parcel.writeString(calorie);
        parcel.writeString(fat);
        parcel.writeString(nutrition);
        parcel.writeString(introduction);
        parcel.writeString(type);
        parcel.writeInt(star);
        parcel.writeInt(streetStar);
        parcel.writeInt(envirStar);
        parcel.writeInt(safeStar);
        parcel.writeString(state);
        parcel.writeInt(likeCount);
        parcel.writeInt(commentCount);
        parcel.writeInt(hasLike);
        parcel.writeString(path);
        parcel.writeString(location);
        parcel.writeString(routepicStr);
        parcel.writeString(activity);
        parcel.writeString(views);
        parcel.writeString(title);
        parcel.writeString(tags);
    }
}
