package com.buxingzhe.pedestrian.bean;

import java.io.Serializable;

/**
 * Created by quanjing on 2017/2/28.
 */
public class AdvCommunityData implements Serializable {
    public long beginDate;
    public long creatTime;
    public long endDate;
    public int id;
    public int imageCount;
    public String introduce;
    public String logoUrl;
    public String name;
    public int orders;
    public String summary;
    public String tag;
    public int userCount;
    public long topImageId;
    public String topImageUrl;
    public String page;//type=1时的活动首页地址
    public int type;//1:http链接;2:活动详情
    public int showTime;
}
