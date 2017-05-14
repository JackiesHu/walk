package com.buxingzhe.pedestrian.bean.walk;

import java.util.List;

/**
 * Created by chinaso on 2017/5/14.
 */

public class WalkHistoryStepEntity {

    /**
     * code : 0
     * content : [{"id":"56040e63f4694fe2aeefd4721350d99c","createTime":1490104110000,"stepCount":16400,"distance":48.5,"userId":"13df725492f04bba936d0736bb625c20","publishDate":"20170322"},{"id":"b54a6d7f7a864a7cad3da464304c463c","createTime":1490103782000,"stepCount":12450,"distance":45.4,"userId":"13df725492f04bba936d0736bb625c20","publishDate":"20170322"}]
     */

    private int code;
    private List<ContentBean> content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 56040e63f4694fe2aeefd4721350d99c
         * createTime : 1490104110000
         * stepCount : 16400
         * distance : 48.5
         * userId : 13df725492f04bba936d0736bb625c20
         * publishDate : 20170322
         */

        private String id;
        private long createTime;
        private int stepCount;
        private double distance;
        private String userId;
        private String publishDate;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }
    }
}
