package com.buxingzhe.pedestrian.bean.activity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QJ on 2017/4/13.
 */

public class WalkActivitiesInfo implements Serializable{

    /**
     * pageNo : 1
     * pageSize : 10
     * count : 7
     * list : [{"id":"8d26be4c2ea24ce797c52164f0778687","createTime":"1489579837","title":"11","introduction":"","publisher":{"id":"e2f835b965e34b2fb8d6cbc22378f55a","nickName":"Apoptoxin","avatarUrl":"http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100"},"isOutDate":"1","attenderCount":0,"startTimeStamp":1490099048,"endTimestamp":1490100600,"banner":"http://115.28.109.174:8282/image/201703154f9de2.png","createTimestamp":1489579837},{"id":"2b53345ffece4984a9eef018863867a2","createTime":"1489578986","title":"111","introduction":"45612346","publisher":{"id":"e2f835b965e34b2fb8d6cbc22378f55a","nickName":"Apoptoxin","avatarUrl":"http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100"},"isOutDate":"1","attenderCount":2,"startTimeStamp":1490098016,"endTimestamp":1458561000,"banner":"http://115.28.109.174:8282/image/201703154f9de2.png","createTimestamp":1489578986},{"id":"54e80908acf8455d8ee24701d9e149ac","createTime":"1489807671","title":"首页活动更新测试","introduction":"这是一个测试活动","publisher":{"id":"e2f835b965e34b2fb8d6cbc22378f55a","nickName":"Apoptoxin","avatarUrl":"http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100"},"isOutDate":"1","attenderCount":0,"startTimeStamp":1490097991,"endTimestamp":1458561000,"banner":"http://115.28.109.174:8282/image/201703188c5d37.png","createTimestamp":1489807671},{"id":"b629f4379e3447a59d09849002595724","createTime":"1489807360","title":"测试活动","introduction":"这是一个测试活动\n","publisher":{"id":"e2f835b965e34b2fb8d6cbc22378f55a","nickName":"Apoptoxin","avatarUrl":"http://q.qlogo.cn/qqapp/1104296289/26C346A929894708488B33195AF1D297/100"},"isOutDate":"1","attenderCount":0,"startTimeStamp":1490098006,"endTimestamp":1458561000,"banner":"http://115.28.109.174:8282/image/201703186c08af.png","createTimestamp":1489807360},{"id":"62266f47b3754b86ba17de2e9bbdbd6b","createTime":"1490868651","title":"测试参与活动","introduction":"正在开始的活动","publisher":{"id":"d2c4fb79bab141f79e58d7f74ba203a3","nickName":"空之轨迹","avatarUrl":"http://115.28.109.174:8282/image/20170331a91030.png"},"isOutDate":"1","attenderCount":2,"startTimeStamp":1490716,"endTimestamp":1493913,"banner":"http://115.28.109.174:8282/image/20170330a4af09.png","createTimestamp":1490868651},{"id":"562a4eac729c4511a5b8b9b5618bb243","createTime":"1491092102","title":"11","introduction":"111111111111","publisher":{"id":"974e0e2c9ca54bdf8c5252fd5dd49d0e","nickName":"Test","avatarUrl":"http://115.28.109.174:8282/image/201704036e5fad.png"},"isOutDate":"1","attenderCount":2,"startTimeStamp":1489852,"endTimestamp":1491494,"banner":"http://115.28.109.174:8282/image/201704024f7b3d.png","createTimestamp":1491092102},{"id":"36bed6c801f343ccbe78191cca951009","createTime":"1490694471","title":"我发布的活动","introduction":"测试","publisher":{"id":"d2c4fb79bab141f79e58d7f74ba203a3","nickName":"空之轨迹","avatarUrl":"http://115.28.109.174:8282/image/20170331a91030.png"},"isOutDate":"1","attenderCount":0,"startTimeStamp":1490630,"endTimestamp":1490803,"banner":"http://115.28.109.174:8282/image/201703287482cb.png","createTimestamp":1490694471}]
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private List<WalkActivityInfo> list;

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

    public List<WalkActivityInfo> getList() {
        return list;
    }

    public void setList(List<WalkActivityInfo> list) {
        this.list = list;
    }
}
