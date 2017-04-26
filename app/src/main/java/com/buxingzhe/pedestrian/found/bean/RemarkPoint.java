package com.buxingzhe.pedestrian.found.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

/**
 * Created by hasee on 2017/4/21.
 */

public class RemarkPoint implements Parcelable {

    /**
     * id : 58c4e12f878a4304abee56fdaf3c52fe
     * createTimestamp : 1489661811000
     * title : 啊啊啊
     * type : 0
     * views : 70
     * star : 4
     * streetStar : 3
     * envirStar : 4
     * safeStar : 5
     * longitude : 116.339303
     * latitude : 39.957423
     * brief : AAAAA级景区
     * introduction : very good
     * user : {"id":"c48ce9c8dfa54fc1bdad206e1f76bf86","avatar":"http://ssss.cn?123.png","nickName":"aaa"}
     * remarkPointTags : 篮球;美食街
     */

    private String id;
    private long createTimestamp;
    private String title;
    private String type;
    private String views;
    private int star;
    private int streetStar;
    private int envirStar;
    private int safeStar;
    private double longitude;
    private double latitude;
    private String brief;
    private String introduction;
    private UserBaseInfo user;
    private String remarkPointTags;

    public RemarkPoint(){

    }

    protected RemarkPoint(Parcel in) {
        id = in.readString();
        createTimestamp = in.readLong();
        title = in.readString();
        type = in.readString();
        views = in.readString();
        star = in.readInt();
        streetStar = in.readInt();
        envirStar = in.readInt();
        safeStar = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
        brief = in.readString();
        introduction = in.readString();
        remarkPointTags = in.readString();
    }

    public static final Creator<RemarkPoint> CREATOR = new Creator<RemarkPoint>() {
        @Override
        public RemarkPoint createFromParcel(Parcel in) {
            return new RemarkPoint(in);
        }

        @Override
        public RemarkPoint[] newArray(int size) {
            return new RemarkPoint[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public UserBaseInfo getUser() {
        return user;
    }

    public void setUser(UserBaseInfo user) {
        this.user = user;
    }

    public String getRemarkPointTags() {
        return remarkPointTags;
    }

    public void setRemarkPointTags(String remarkPointTags) {
        this.remarkPointTags = remarkPointTags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(createTimestamp);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(views);
        dest.writeInt(star);
        dest.writeInt(streetStar);
        dest.writeInt(envirStar);
        dest.writeInt(safeStar);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(brief);
        dest.writeString(introduction);
        dest.writeString(remarkPointTags);
    }
}
