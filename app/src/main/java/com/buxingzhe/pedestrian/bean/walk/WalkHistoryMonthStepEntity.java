package com.buxingzhe.pedestrian.bean.walk;

import java.util.List;

/**
 * Created by chinaso on 2017/5/15.
 */

public class WalkHistoryMonthStepEntity {

    /**
     * code : 0
     * content : [{"stepCount":0,"distance":0,"publishDate":"201605"},{"stepCount":0,"distance":0,"publishDate":"201606"},{"stepCount":0,"distance":0,"publishDate":"201607"},{"stepCount":0,"distance":0,"publishDate":"201608"},{"stepCount":0,"distance":0,"publishDate":"201609"},{"stepCount":0,"distance":0,"publishDate":"201610"},{"stepCount":0,"distance":0,"publishDate":"201611"},{"stepCount":0,"distance":0,"publishDate":"201612"},{"stepCount":0,"distance":0,"publishDate":"201701"},{"stepCount":0,"distance":0,"publishDate":"201702"},{"stepCount":0,"distance":0,"publishDate":"201703"},{"stepCount":1219,"distance":0.8688815988101997,"publishDate":"201704"},{"stepCount":37280,"distance":50.22202377925161,"publishDate":"201705"}]
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
         * stepCount : 0
         * distance : 0
         * publishDate : 201605
         */

        private int stepCount;
        private double distance;
        private String publishDate;

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

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }
    }
}
