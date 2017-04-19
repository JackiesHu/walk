package com.buxingzhe.pedestrian.bean.activity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QJ on 2017/4/19.
 */

public class WalkRecordsByActivity implements Serializable{
    /**
     * pageNo : 1
     * pageSize : 10
     * count : 2
     * list : [{"id":"6c72f05fc7e543f7a950edc51196bdd0","createTime":"1492583455","user":{"id":"ac84f838f54c4122911b15459c808cce","nickName":"The has the","avatarUrl":"http://115.28.109.174:8282/image/2017032052320a.png"},"activity":"2b53345ffece4984a9eef018863867a2","stepCount":21000,"distance":100,"duration":"100","altitudeAsend":"100","altitudeHigh":"100","altitudeLow":"100","calorie":"100","fat":"100","nutrition":"100","introduction":"5","type":"0","title":"洪崖洞美食街","star":5,"streetStar":2,"envirStar":2,"safeStar":2,"state":"0","likeCount":0,"commentCount":1,"tags":"美不胜收;吃货的天堂;天天跑步","hasLike":0,"location":"321","views":"http://115.28.109.174:8282/image/20170317ccb94c.png","routepicStr":"http://115.28.109.174:8282/image/20170317ccb94c.png"},{"id":"055950f8691b463c9a2676365d08e723","createTime":"1490798028","user":{"id":"ac84f838f54c4122911b15459c808cce","nickName":"The has the","avatarUrl":"http://115.28.109.174:8282/image/2017032052320a.png"},"activity":"2b53345ffece4984a9eef018863867a2","stepCount":11000,"distance":1245,"duration":"100","altitudeAsend":"12","altitudeHigh":"456","altitudeLow":"132","calorie":"100","fat":"100","nutrition":"100","introduction":"很好玩","type":"0","title":"山城步行","star":3,"streetStar":2,"envirStar":1,"safeStar":2,"state":"0","likeCount":2,"commentCount":2,"tags":"美不胜收;吃货的天堂;天天跑步","hasLike":0,"location":"111","views":"http://115.28.109.174:8282/image/20170317ccb94c.png","routepicStr":"http://115.28.109.174:8282/image/20170317ccb94c.png"}]
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private List<WalkRecordByActivity> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<WalkRecordByActivity> getList() {
        return list;
    }

    public void setList(List<WalkRecordByActivity> list) {
        this.list = list;
    }


}
