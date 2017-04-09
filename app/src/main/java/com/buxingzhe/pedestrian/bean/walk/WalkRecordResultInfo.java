package com.buxingzhe.pedestrian.bean.walk;

/**
 * 步行或者骑行记录的 数据Bean
 * Created by zhaishaoping on 04/04/2017.
 */
public class WalkRecordResultInfo {
    private String id;//记录Id
    private String isNewRecord;//客户端忽略此字段
    private String createTime;//记录创建时间
    private UserInfo user;//记录创建用户的基本信息
    private String activity;//该记录所属活动ID,若自发的步行则无此字段
    private String stepCount;//步行数，骑行无此字段
    private String distance;//距离
    private String duration;//持续时间
    private String altitudeAsend;//海拔上升
    private String altitudeHigh;//最高海拔
    private String altitudeLow;//最低海拔
    private String calorie;//卡路里
    private String fat;//脂肪
    private String nutrition;//蛋白质
    private String views;//风景的url,最多三张，用';'分割
    private String introduction;//介绍
    private String type;//0：步行，1：骑行
    private String routepicStr;//线路图片URL，1张
    private String title;//记录标题
    private String star;//星级
    private String streetStar;//街道星级
    private String safeStar;//步行安全
    private String envirStar;//环境舒适
    private String state;//状态 0：正常，1：已删除
    private String likeCount;//点赞次数
    private String commentCount;//评论次数
    private String location;//步行记录位置


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(String isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
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

    public String getStreetStar() {
        return streetStar;
    }

    public void setStreetStar(String streetStar) {
        this.streetStar = streetStar;
    }

    public String getSafeStar() {
        return safeStar;
    }

    public void setSafeStar(String safeStar) {
        this.safeStar = safeStar;
    }

    public String getEnvirStar() {
        return envirStar;
    }

    public void setEnvirStar(String envirStar) {
        this.envirStar = envirStar;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private class UserInfo {
        private String id;//用户ID
        private String isNewRecord;//客户端忽略此字段
        private String avatar;//用户头像
        private String nickName;//用户名

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(String isNewRecord) {
            this.isNewRecord = isNewRecord;
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
    }
}
