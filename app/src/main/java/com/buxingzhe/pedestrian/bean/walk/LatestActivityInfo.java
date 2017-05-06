package com.buxingzhe.pedestrian.bean.walk;

/**
 * Created by chinaso on 2017/5/6.
 */

public class LatestActivityInfo {

    /**
     * code : 0
     * content : {"id":"1eb7977a9ad54d2f8eb6a48381afaf74","title":"111","introduction":"45612346","banner":"http://115.28.109.174:8282/image/20170315828304.364468_39.970859_00101_131_2014811.png","publisher":{"id":"6acedc886fbb40f3b21569646ae9f228","avatar":"eqwheq","nickName":"xiaoming"},"isOutDate":"0","startTimeStamp":1489393362000,"endTimestamp":1489393362000,"createTimestamp":1489567591000}
     */

    private int code;
    private ContentBean content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 1eb7977a9ad54d2f8eb6a48381afaf74
         * title : 111
         * introduction : 45612346
         * banner : http://115.28.109.174:8282/image/20170315828304.364468_39.970859_00101_131_2014811.png
         * publisher : {"id":"6acedc886fbb40f3b21569646ae9f228","avatar":"eqwheq","nickName":"xiaoming"}
         * isOutDate : 0
         * startTimeStamp : 1489393362000
         * endTimestamp : 1489393362000
         * createTimestamp : 1489567591000
         */

        private String id;
        private String title;
        private String introduction;
        private String banner;
        private PublisherBean publisher;
        private String isOutDate;
        private long startTimeStamp;
        private long endTimestamp;
        private long createTimestamp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
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

        public long getStartTimeStamp() {
            return startTimeStamp;
        }

        public void setStartTimeStamp(long startTimeStamp) {
            this.startTimeStamp = startTimeStamp;
        }

        public long getEndTimestamp() {
            return endTimestamp;
        }

        public void setEndTimestamp(long endTimestamp) {
            this.endTimestamp = endTimestamp;
        }

        public long getCreateTimestamp() {
            return createTimestamp;
        }

        public void setCreateTimestamp(long createTimestamp) {
            this.createTimestamp = createTimestamp;
        }

        public static class PublisherBean {
            /**
             * id : 6acedc886fbb40f3b21569646ae9f228
             * avatar : eqwheq
             * nickName : xiaoming
             */

            private String id;
            private String avatar;
            private String nickName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
}
