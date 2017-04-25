package com.buxingzhe.pedestrian.found.bean;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

import java.util.List;

/**
 * Created by hasee on 2017/4/24.
 */

public class PointComment {

    private int pageNo;//1
    private int pageSize;//": 20,
    private int count;//2,
    private List<Comment> list;//

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static class Comment {
        /**
         * id : ed7ec29cd9d643cd9f2c7abe92c779f0
         * createTime : 1490170070000
         * user : {"id":"13df725492f04bba936d0736bb625c20","avatar":"eqwheq","nickName":"xiaoming"}
         * remarkPoint : 50f995b3707745c8848bf80d2e7c7f8b
         * safeStar : 4
         * streetStar : 4
         * envirStar : 4
         * content : 这是评论2
         */

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


}
